package app.controllers.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.javalite.activeweb.annotations.GET;
import org.javalite.activeweb.annotations.POST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.Const;
import app.base.PlatformController;
import app.models.Comment;
import app.models.Lang;
import app.models.Partner;
import app.models.Question;
import app.models.Tag;
import app.models.Translation;
import app.util.JsonHelper;
import app.util.StringUtils;

public class QuestionsController extends PlatformController {

	static final Logger log = LoggerFactory.getLogger(QuestionsController.class);
	protected static final String[] LIST_EXCLUDED_FIELDS = { "html_content_en", "html_content_id", "picture_path",
			"created_at", "updated_at" };
	protected static final String[] EXCLUDED_FIELDS = { "created_at", "updated_at" };

	/**
	 * Render AngularJS SPA (the rest of the methods are JSON-based)
	 * 
	 * @see /views/platform/questions/index.ftl
	 * @see /app/platform/questions/*.js etc.
	 */
	public void index() {
		view("languages", Lang.findAll().orderBy("label_en asc"));
	}

	@GET
	public void archived() {
		questionsList(" is_archived=true ");
	}

	@GET
	public void published() {
		questionsList(" is_published=true ");
	}

	@GET
	public void proposed() {
		questionsList(" is_archived=false AND is_published=false ");
	}

	@GET
	public void mine() {
		Question authenticatedPartner = (Question) session(Const.AUTHENTICATED_PARTNER);
		if (authenticatedPartner != null) {
			questionsList(" proposed_by=" + authenticatedPartner.getLongId());
		} else {
			json_403();
		}
	}

	@GET
	public void by() {
		String partnerParam = param("partner");
		if (!StringUtils.nullOrEmpty(partnerParam)) {
			try {
				Long partnerId = Long.decode(partnerParam);
				questionsList(" proposed_by=" + partnerParam);
			}
			catch (NumberFormatException nfe) {
				json_400("wrong partner ID " + partnerParam);
			}
		} else {
			json_400("missing partner ID ");
		}
	}

	@GET
	public void edit() {
		String idParam = param("id");
		try {
			if (!StringUtils.nullOrEmpty(idParam)) {
				Long questionId = Long.decode(idParam);
				List<Question> questions = Question.where("id=?", questionId).include(Comment.class, Tag.class,
						Translation.class);
				Question question = questions.get(0);
				if (question != null) {
					//cache children
					question.getAll(Comment.class);
					//question.getAll(Upvote.class); <-- we don't need this one...
					question.getAll(Tag.class);
					question.getAll(Translation.class);
					returnJson(Question.getMetaModel(), question, EXCLUDED_FIELDS);
				} else {
					json_404("cannot find question with id=" + idParam);
				}
			}
		}
		catch (NumberFormatException nfe) {
			json_400("invalid question id: " + idParam);
		}

	}

	@POST
	public void save() {
		Map atts = Collections.emptyMap();
		Long id = null;
		Partner me = (Partner) session(Const.AUTHENTICATED_PARTNER);
		if (me == null) {
			json_403();
			return;
		}

		try {
			atts = JsonHelper.toMap(getRequestString());
			if (atts != null && atts.containsKey("id")) {
				id = Long.decode(atts.get("id").toString());
			}
		}
		catch (IOException e) {
			json_400("Cannot read POST payload");
		}
		catch (NumberFormatException nfe) {
			json_400("Malformed question id");
		}

		if (atts == null) json_400("Cannot read POST payload");

		Base.openTransaction();
		Question question = new Question();
		if (id != null) {//update
			question = Question.findById(id);
			if (question == null) {
				Base.rollbackTransaction();
				json_404(String.format("No existing question with id: %d", question.getLongId()));
				return;
			}
			//cleanup the children in preparation for addition from the values map
			for (Comment o : question.getAll(Comment.class)) {
				question.remove(o);
			}
			for (Tag o : question.getAll(Tag.class)) {
				question.remove(o);
			}
			for (Translation o : question.getAll(Translation.class)) {
				question.remove(o);
			}
			question.save();
		}

		//hydrate common attributes 
		question.fromMap(atts);

		if (id == null) {//new question
			question.set("proposed_by", me.getLongId());
		} else {

		}
		boolean succ = question.save();
		if (!succ) {
			Base.rollbackTransaction();
			json_400("cannot update question details: " + question.errors().toString());
			return;
		}

		//process translations, tags and comments...
		if (atts.containsKey("translations")) {
			for (Map m : (List<Map>) atts.get("translations")) {
				Translation o = new Translation();
				o.fromMap(m);
				if (o.get("created_by") == null) {
					o.setLong("created_by", me.getLongId());
				}
				question.add(o);
			}
		}
		if (atts.containsKey("comments")) {
			for (Map m : (List<Map>) atts.get("comments")) {
				Comment o = new Comment();
				o.fromMap(m);
				question.add(o);
			}
		}
		if (atts.containsKey("tags")) {
			for (Map m : (List<Map>) atts.get("tags")) {
				Tag o = new Tag();
				o.fromMap(m);
				question.add(o);
			}
		}
		succ = question.save();
		if (!succ) {
			Base.rollbackTransaction();
			json_400("cannot update question associations: " + question.errors().toString());
			return;
		}
		Base.commitTransaction();
		json_200(String.format("updated question with id: %d", question.getLongId()));

	}

	//------------//

	protected void questionsList(String filter) {
		List<Object> filterParams = new ArrayList<Object>();
		String searchParam = param("search");
		if (!StringUtils.nullOrEmpty(searchParam) && searchParam.matches("\\w+")) {
			filter += " AND (lower(title_en) like '%?%' OR lower(description_en) '%?%') ";
			filterParams.add(searchParam.toLowerCase());
			filterParams.add(searchParam.toLowerCase());
		}
		returnJsonResults(Question.getMetaModel(), Question.find(filter, filterParams),
				Question.count(filter, filterParams), LIST_EXCLUDED_FIELDS);
	}

}
