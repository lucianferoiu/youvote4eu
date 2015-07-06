package app.config;

import org.javalite.activeweb.AbstractRouteConfig;
import org.javalite.activeweb.AppContext;

import app.controllers.platform.HomeController;

public class RouteConfig extends AbstractRouteConfig {

	@Override
	public void init(AppContext appContext) {
		route("/platform").to(HomeController.class).action("catchall");
		ignore("/platform/lorem").exceptIn("development");
		route("/question/{id}").to(app.controllers.HomeController.class).action("question").get();
		route("/archived/{id}").to(app.controllers.HomeController.class).action("archived").get();
		route("/vote/{id}/{value}").to(app.controllers.HomeController.class).action("vote").put();
	}
}
