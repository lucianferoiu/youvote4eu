package app.config;

import org.javalite.activeweb.AbstractControllerConfig;
import org.javalite.activeweb.AppContext;
import org.javalite.activeweb.controller_filters.AbstractLoggingFilter.Level;
import org.javalite.activeweb.controller_filters.HeadersLogFilter;
import org.javalite.activeweb.controller_filters.TimingFilter;

import app.controllers.AuthCookieFilter;

public class AppControllerConfig extends AbstractControllerConfig {

	@Override
	public void init(AppContext context) {
		addGlobalFilters(new TimingFilter());
		addGlobalFilters(new HeadersLogFilter(Level.DEBUG, true));
		// addGlobalFilters(new RequestPropertiesLogFilter());
		// add(new DBConnectionFilter()).to(XYZController.class);
		addGlobalFilters(new AuthCookieFilter());
		// add(new AuthCookieFilter(Const.PARTNER_AUTH_COOKIE_NAME, 60 * 60 * 24 * 14)).to(HomeController.class);
	}
}
