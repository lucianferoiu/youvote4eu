package app.config;

import org.javalite.activeweb.AbstractControllerConfig;
import org.javalite.activeweb.AppContext;
import org.javalite.activeweb.controller_filters.AbstractLoggingFilter.Level;
import org.javalite.activeweb.controller_filters.DBConnectionFilter;
import org.javalite.activeweb.controller_filters.HeadersLogFilter;
import org.javalite.activeweb.controller_filters.TimingFilter;

import app.controllers.filters.AuthCookieFilter;
import app.controllers.filters.PlatformAuthFilter;

public class AppControllerConfig extends AbstractControllerConfig {

	@Override
	public void init(AppContext context) {
		addGlobalFilters(new TimingFilter());
		addGlobalFilters(new HeadersLogFilter(Level.DEBUG, true));
		// addGlobalFilters(new RequestPropertiesLogFilter());
		addGlobalFilters(new DBConnectionFilter());
		addGlobalFilters(new AuthCookieFilter());
		addGlobalFilters(new PlatformAuthFilter());
	}
}
