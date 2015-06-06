package app.config;

import org.javalite.activeweb.AbstractRouteConfig;
import org.javalite.activeweb.AppContext;

import app.controllers.platform.HomeController;

public class RouteConfig extends AbstractRouteConfig {

	@Override
	public void init(AppContext appContext) {
		route("/platform").to(HomeController.class).action("catchall");
	}
}
