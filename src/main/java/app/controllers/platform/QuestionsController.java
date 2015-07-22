package app.controllers.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javalite.activejdbc.Base;
import org.javalite.activeweb.annotations.GET;
import org.javalite.activeweb.annotations.POST;
import org.javalite.activeweb.annotations.PUT;
import org.javalite.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.Const;
import app.base.PlatformController;
import app.models.Comment;
import app.models.Lang;
import app.models.Partner;
import app.models.Question;
import app.models.QuestionsTags;
import app.models.Tag;
import app.models.Translation;
import app.models.Upvote;
import app.util.JsonHelper;
import app.util.StringUtils;

public class QuestionsController extends PlatformController {

	static final Logger log = LoggerFactory.getLogger(QuestionsController.class);
	protected static final String[] LIST_EXCLUDED_FIELDS = { "html_content_en", "html_content_id", "picture_path", "created_at",
			"updated_at" };
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
		questionsList(" is_archived=true ", " archived_at desc, popular_votes desc ");
	}

	@GET
	public void published() {
		questionsList(" is_published=true AND (is_archived IS NULL OR is_archived=false) ", " popular_votes desc, open_at desc ");
	}

	@GET
	public void proposed() {
		questionsList(" (is_archived IS NULL OR is_archived=false) AND (is_published IS NULL OR is_published=false) ",
				" support desc, created_at desc ");
	}

	@GET
	public void mine() {
		Partner authenticatedPartner = (Partner) session(Const.AUTHENTICATED_PARTNER);
		if (authenticatedPartner != null) {
			questionsList(" proposed_by=" + authenticatedPartner.getLongId(), " created_at desc, support desc ");
		} else {
			log.debug("No authenticated Partner on the session");
			json_403();
		}
	}

	@GET
	public void by() {
		String partnerParam = param("partner");
		if (!StringUtils.nullOrEmpty(partnerParam)) {
			try {
				Long partnerId = Long.decode(partnerParam);
				questionsList(" proposed_by=" + partnerParam, " created_at desc, support desc ");
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
				List<Question> questions = Question.where("id=?", questionId)//
						.include(Comment.class, Tag.class, Translation.class);//gather translations and metadata associated entities
				Question question = questions.get(0);
				if (question != null) {
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
		Long myId = me.getLongId();

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
				log.debug("Cannot find question {} for saving", id);
				json_404(String.format("No existing question with id: %d", question.getLongId()));
				return;
			}
		}

		//hydrate common attributes 
		question.fromMap(atts);
		parseTimestamps(question, atts);
		if (id == null) {//new question
			question.set("proposed_by", myId);
			question.setLong("support", 1L);
			question.setBoolean("is_published", false);
			question.setBoolean("is_archived", false);
			question.setBoolean("is_deleted", false);
			question.setLong("popular_votes", 0L);
			log.debug("Creating a new question proposed by {} ...", myId);
		} else {
			log.debug("Updating question {} ... ", id);
		}

		boolean succ = question.save();
		if (!succ) {
			Base.rollbackTransaction();
			json_400("cannot update question details: " + question.errors().toString());
			return;
		}

		//process translations, tags and comments...
		if (atts.containsKey("children")) {
			Map kids = (Map) atts.get("children");
			if (kids.containsKey("translations")) {
				for (Map m : (List<Map>) kids.get("translations")) {
					Translation o = new Translation();
					o.fromMap(m);
					if (o.get("created_by") == null) {
						o.setLong("created_by", myId);
					}
					question.add(o);//always insert/update a translation - no deletions...
					log.debug("{} a translation of '{}' for language '{}' to the question {}", //
							(o.getLongId() == null ? "Adding" : "Updating"), o.get("field_type"), o.get("lang"), id);
				}
			}
			if (kids.containsKey("comments")) {
				List<Comment> existingComments = question.getAll(Comment.class);
				Set<Long> existingCommentIDs = new HashSet<>();
				for (Comment existingComment : existingComments) {
					Long cId = existingComment.getLongId();
					existingCommentIDs.add(cId);
				}
				Set<Long> toKeep = new HashSet<>();
				for (Map m : (List<Map>) kids.get("comments")) {
					Comment o = new Comment();
					o.fromMap(m);
					Object oId = o.getId();
					Long lId = oId != null ? o.getLongId() : null;
					if (lId != null && existingCommentIDs.contains(lId)) {//we keep this one
						toKeep.add(lId);
					} else {//add it
						o.set("created_by", myId);
						question.add(o);
						log.debug("New comment '{}' added by partner {} to the question {}", o.get("text"), myId, id);
					}
				}
				for (Comment existingComment : existingComments) {
					Long tId = existingComment.getLongId();
					if (!toKeep.contains(tId)) {
						if (!existingComment.frozen()) {
							log.debug("Comment '{}' initially created by partner {} will be removed from question {} by partner {}",
									existingComment.get("text"), existingComment.get("created_by"), id, myId);
							question.remove(existingComment);
						}
					}
				}
			}
			if (kids.containsKey("tags")) {
				List<Tag> existingTags = question.getAll(Tag.class);
				Set<Long> existingTagIDs = new HashSet<>();
				for (Tag existingTag : existingTags) {
					Long tId = existingTag.getLongId();
					existingTagIDs.add(tId);
				}
				Set<Long> toKeep = new HashSet<>();
				for (Map m : (List<Map>) kids.get("tags")) {
					Tag o = new Tag();
					o.fromMap(m);
					if (o.getId() != null) {//we keep this one
						Long oId = o.getLongId();
						if (existingTagIDs.contains(oId)) {
							toKeep.add(oId);
						} else {//new tag - create the association
							QuestionsTags qt = QuestionsTags.createIt("created_by", myId, "question_id", id, "tag_id", oId);
							log.debug("Tag '{}' added by partner {} to the question {}", o.get("text"), myId, id);
						}
					} else {//new tag
						o.saveIt();
						QuestionsTags qt = QuestionsTags.createIt("created_by", myId, "question_id", id, "tag_id", o.getLongId());
						log.debug("New tag '{}' was created by partner {} and added to the question {}", o.get("text"), myId, id);

					}
				}
				for (Tag existingTag : existingTags) {
					Long tId = existingTag.getLongId();
					if (!toKeep.contains(tId)) {
						question.remove(existingTag);
						log.debug("Partner {} removed tag '{}' from the question {}", myId, existingTag.get("text"), id);
					}
				}
			}
		}
		succ = question.save();
		if (!succ) {
			Base.rollbackTransaction();
			json_400("cannot update question associations: " + question.errors().toString());
			return;
		}
		Base.commitTransaction();
		log.debug("Question {} processed and saved successfully (commit)", id);
		json_200(String.format("updated question with id: %d", question.getLongId()));

	}

	@GET
	public void canUpvote() {
		Partner me = (Partner) session(Const.AUTHENTICATED_PARTNER);
		if (me == null) {
			json_403();
			return;
		}
		Long myId = me.getLongId();

		List<String> qParams = params("questionIDs");
		List<Long> qIDs = new ArrayList<Long>();
		for (String qParam : qParams) {
			try {
				qIDs.add(Long.parseLong(qParam));
			}
			catch (NumberFormatException nfe) {}
		}

		List<Long> canVoteIDs = Collections.emptyList();
		if (!qIDs.isEmpty()) {
			String questionsIDs = Util.join(qIDs, ",");
			canVoteIDs = Base.firstColumn(" SELECT DISTINCT q.id FROM questions q " + // get the IDs of ..
					" WHERE q.is_published=false AND q.proposed_by!=? AND q.id in (" + questionsIDs + ") " + // unpublished questions not proposed by the upvoter
					" EXCEPT " + // but remove...
					"SELECT u.question_id FROM upvotes u WHERE u.upvoted_by=? AND u.question_id in (" + questionsIDs + ") ", // already upvoted questions by the upvoter
					myId, myId);
			Collections.sort(canVoteIDs);

		}
		returnJsonList(canVoteIDs);

	}

	@PUT
	public void upvote() {
		Partner me = (Partner) session(Const.AUTHENTICATED_PARTNER);
		if (me == null) {
			json_403();
			return;
		}

		String idParam = "";
		try {
			idParam = getRequestString();
			if (!StringUtils.nullOrEmpty(idParam)) {
				Long questionId = Long.decode(idParam);
				Question question = Question.findById(questionId);
				if (question != null) {
					Long myId = me.getLongId();
					if (question.getLong("proposed_by") == myId) {
						json_400("the autohor of a question cannot upvote it");
						return;
					}
					Long alreadyUpvotedCount = Upvote.count("question_id=? AND upvoted_by=?", questionId, myId);
					if (alreadyUpvotedCount > 0) {
						json_400("partner already upvoted the question");
						return;
					}

					//we can upvote
					Base.openTransaction();
					Upvote upvote = Upvote.create("question_id", questionId, "upvoted_by", myId, "value", 1);//if we implement a reputation multiplier, here's the place for that reputation to act
					question.add(upvote);
					question.setLong("support", question.getLong("support") + 1);
					question.save();
					Base.commitTransaction();
					json_204();
				} else {
					json_404("cannot find question with id=" + idParam);
				}
			}
		}
		catch (NumberFormatException nfe) {
			log.warn("invalid question id ", nfe);
			json_400("invalid question id: " + idParam);
		}
		catch (IOException e) {
			log.warn("cannot read PUT payload of question id ", e);
			json_400("cannot read question id");
		}

	}

	//------------//

	protected void questionsList(String filter, String orderBy) {
		List<Object> filterParams = new ArrayList<Object>();
		//exclude deleted...
		filter += "AND (is_deleted IS NULL OR is_deleted=?) ";
		filterParams.add(new Boolean(false));

		String searchParam = param("search");
		if (!StringUtils.nullOrEmpty(searchParam) && searchParam.matches("\\w+")) {
			searchParam = searchParam.toLowerCase();
			try {
				//shortcut for the question id
				Long qId = Long.parseLong(searchParam);
				filter += " AND id=? ";
				filterParams.add(qId);
			}
			catch (NumberFormatException nfe) {
				filter += " AND (lower(title) like '%" + searchParam + "%' OR lower(description) like '%" + searchParam + "%') ";
				// filterParams.add(searchParam);
				// filterParams.add(searchParam);
			}
		}
		returnJsonResults(Question.getMetaModel(), Question.find(filter, filterParams.toArray()).orderBy(orderBy),
				Question.count(filter, filterParams.toArray()), LIST_EXCLUDED_FIELDS);
	}

}
