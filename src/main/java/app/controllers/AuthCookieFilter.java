package app.controllers;

import org.javalite.activeweb.Cookie;
import org.javalite.activeweb.controller_filters.HttpSupportFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.Const;
import app.services.TokenGenerator;
import app.util.StringUtils;

import com.google.inject.Inject;

/**
 * Intercepts all HTTP traffic and if missing, sets the authentication token as
 * a cookie
 */
public class AuthCookieFilter extends HttpSupportFilter {

	static final Logger log = LoggerFactory.getLogger(AuthCookieFilter.class);

	@Inject
	protected TokenGenerator tokenGenerator;

	@Override
	public void before() {
		Cookie c = cookie(Const.AUTH_COOKIE_NAME);
		if (c == null) {
			setAuthCookie();
		} else {
			String tok = c.getValue();
			if (StringUtils.nullOrEmpty(tok) || tok.length() < 32) {
				log.warn("Empty or suspicious token value \"{}\" - resetting... ", tok);
				setAuthCookie();
			}
		}
	}

	protected void setAuthCookie() {
		Cookie c;
		String token = tokenGenerator.generateToken();
		c = new Cookie(Const.AUTH_COOKIE_NAME, token, true);
		c.setMaxAge(60 * 60 * 24 * 365 * 3);//lives for 3 years
		c.setVersion(1);
		c.setSecure(true);
		log.debug("Setting cookie {} to client ", c.toString());
		sendCookie(c);
	}

}
