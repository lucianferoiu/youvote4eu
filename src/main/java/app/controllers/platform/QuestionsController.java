package app.controllers.platform;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activeweb.annotations.GET;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.Const;
import app.base.PlatformController;
import app.models.Lang;
import app.models.Partner;
import app.models.Question;
import app.util.StringUtils;

public class QuestionsController extends PlatformController {

	static final Logger log = LoggerFactory.getLogger(QuestionsController.class);
	protected static final String[] LIST_EXCLUDED_FIELDS = { "html_content_en", "html_content_id", "picture_path",
			"created_at", "updated_at" };

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
		Partner authenticatedPartner = (Partner) session(Const.AUTHENTICATED_PARTNER);
		if (authenticatedPartner != null) {
			questionsList(" proposed_by=" + authenticatedPartner.getLongId());
		} else {
			json_403();
		}
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
