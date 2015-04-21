package app.controllers.filters;

import org.javalite.activeweb.Cookie;
import org.javalite.activeweb.controller_filters.HttpSupportFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.Const;
import app.models.Partner;
import app.util.StringUtils;

/**
 * Intercepts all HTTP traffic and denies unauthenticated & unauthorized access.
 * If authentication cookie is present but no <code>partner</code> object on
 * session it redirects to auto-signin page
 */
public class PlatformAuthFilter extends HttpSupportFilter {
	static final Logger log = LoggerFactory.getLogger(PlatformAuthFilter.class);

	@Override
	public void before() {
		if (path().startsWith(Const.PLATFORM_PATH)) {//only protect the /platform area
			Partner authenticatedPartner = (Partner) session(Const.AUTHENTICATED_PARTNER);
			Cookie authToken = cookie(Const.PARTNER_AUTH_COOKIE_NAME);
			if (authToken == null) {//no "remember me" cookie
				if (authenticatedPartner == null) {//not authenticated yet
					authenticationRequired();
				} else {
					ensureAuthorized(authenticatedPartner);//already authorized but didn't opt for "remember me" so let the user be...
				}
			} else {
				String tok = authToken.getValue();
				if (authenticatedPartner == null) {//not authenticated yet
					if (StringUtils.nullOrEmpty(tok) || tok.length() < 32) {//sanity check of the cookie
						log.warn("Empty or suspicious token value \"{}\" - resetting... ", tok);
						authToken.setValue("resetting-this-cookie");
						authToken.setMaxAge(1);//one sec. left to live
						sendCookie(authToken);
						authenticationRequired();
					} else {//we may have a "remember me" cookie on our hands
						if (!"/platform/home/autosignin".equalsIgnoreCase(path())) {
							session(Const.REDIRECT_TO_URL, path());//let's be nice and redirect here after authentication
							redirect("/platform/home/autosignin");
						}
					}

				} else {//already authenticated
					ensureAuthorized(authenticatedPartner);
				}
			}
		}
	}

	protected void ensureAuthorized(Partner authenticatedPartner) {
		if (!path().startsWith("/platform/statistics") && !authenticatedPartner.getBoolean("can_view_statistics")) {
			notAuthorized();
		}
		// TODO restrict access only to authorized sections

	}

	protected void notAuthorized() {
		flash("access_denied", "not_authorized");
		redirect("/platform/home");
	}

	protected void authenticationRequired() {
		if (pathRequiresAuthentication()) {
			flash("access_denied", "authentication_required");
			session(Const.REDIRECT_TO_URL, path());//let's be nice and redirect here after authentication
			flash("should_sign_up", "true");//homepage should already show the sign up form...
			redirect("/platform/home");
		}
	}

	public boolean pathRequiresAuthentication() {
		String path = path();
		if (path.equalsIgnoreCase("/platform/home")) return false;
		if (path.equalsIgnoreCase("/platform/home/index")) return false;
		if (path.equalsIgnoreCase("/platform/home/autosignin")) return false;
		if (path.equalsIgnoreCase("/platform/home/reset_password")) return false;
		if (path.equalsIgnoreCase("/platform/home/validate")) return false;
		if (path.equalsIgnoreCase("/platform/home/signin") && method().equalsIgnoreCase("POST")) return false;
		if (path.equalsIgnoreCase("/platform/home/register") && method().equalsIgnoreCase("POST")) return false;
		return true;
	}

}
