package app.config;

import org.javalite.activeweb.AbstractRouteConfig;
import org.javalite.activeweb.AppContext;

import app.controllers.platform.HomeController;

public class RouteConfig extends AbstractRouteConfig {

	@Override
	public void init(AppContext appContext) {
		// platform urls
		route("/platform").to(HomeController.class).action("catchall");
		ignore("/platform/lorem").exceptIn("development");
		//preferences
		route("/lang/{langCode}").to(app.controllers.PreferencesController.class).action("lang").get();
		//lists of questions
		route("/").to(app.controllers.HomeController.class).action("index").get();
		route("/as/{format}").to(app.controllers.HomeController.class).action("index").get();
		route("/list/{questionsKind}/as/{format}").to(app.controllers.HomeController.class).action("index").get();
		route("/list/tag/{tagId}/as/{format}").to(app.controllers.HomeController.class).action("index").get();
		route("/list/{questionsKind}").to(app.controllers.HomeController.class).action("index").get();
		route("/list/tag/{tagId}").to(app.controllers.HomeController.class).action("index").get();
		//individual questions
		route("/question/{id}").to(app.controllers.HomeController.class).action("question").get();
		route("/archived/{id}").to(app.controllers.HomeController.class).action("archived").get();
		//voting
		route("/vote/{qId}/{voteValue}").to(app.controllers.HomeController.class).action("vote").put();
		route("/cast/vote").to(app.controllers.HomeController.class).action("castVote").post();
		//citizen validation
		route("/send-validation-mail").to(app.controllers.HomeController.class).action("sendValidationEmail").post();
		route("/validate-citizen").to(app.controllers.HomeController.class).action("validateCitizen").get();
		//stats
		route("/q/votes/by/country/{id}").to(app.controllers.StatsController.class).action("votesByCountry").get();
		route("/aq/votes/by/country/{id}").to(app.controllers.StatsController.class).action("votesByCountry").get();
		route("/aq/tally/by/country/{id}").to(app.controllers.StatsController.class).action("tallyByCountry").get();
	}
}
