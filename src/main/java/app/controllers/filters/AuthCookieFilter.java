package app.controllers.filters;

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

	private int maxAge;
	private String cookieName;
	private String path = "/";

	@Inject
	protected TokenGenerator tokenGenerator;

	public AuthCookieFilter() {
		setCookieName(Const.AUTH_COOKIE_NAME);
		setMaxAge(60 * 60 * 24 * 365 * 3);//lives for 3 years
	}

	public AuthCookieFilter(String cookieName, int maxAge) {
		setCookieName(cookieName);
		setMaxAge(maxAge);
	}

	@Override
	public void before() {
		Cookie c = cookie(getCookieName());
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
		c = new Cookie(getCookieName(), token, true);
		c.setMaxAge(getMaxAge());
		c.setVersion(1);
		//		c.setSecure(true); // TODO: uncomment this after setting up the SSL
		c.setPath(getPath());
		log.debug("Setting cookie {} to client ", c.toString());
		sendCookie(c);
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
