package app.controllers.platform;

import app.base.PlatformController;
import app.models.Lang;

public class QuestionsController extends PlatformController {

	/**
	 * Render AngularJS SPA (the rest of the methods are JSON-based)
	 * 
	 * @see /views/platform/questions/index.ftl
	 * @see /app/platform/questions/*.js etc.
	 */
	public void index() {
		view("languages", Lang.findAll().orderBy("code asc"));
	}

}
