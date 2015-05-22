package app.controllers.filters;

import org.javalite.activeweb.Cookie;
import org.javalite.activeweb.controller_filters.HttpSupportFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.Const;
import app.models.Partner;

/**
 * Intercepts all HTTP traffic and denies unauthenticated & unauthorized access.
 * If authentication cookie is present but no <code>partner</code> object on
 * session it redirects to auto-sign-in page
 */
public class PlatformAuthFilter extends HttpSupportFilter {
	static final Logger log = LoggerFactory.getLogger(PlatformAuthFilter.class);

	@Override
	public void before() {
		String path = path();
		if (path.startsWith(Const.PLATFORM_PATH)) {//only protect the /platform area
			Partner authenticatedPartner = (Partner) session(Const.AUTHENTICATED_PARTNER);
			if (authenticatedPartner != null) {//area is already protected by authentication; just filter the authorization
				ensureAuthorized(authenticatedPartner, path);
				return;
			}

			Cookie authToken = cookie(Const.PARTNER_AUTH_COOKIE_NAME);
			if (authToken != null && (!"/platform/auth/autosignin".equals(path))) {//"remember me" cookie but no session partner? autosignin!
				log.debug("Auth filter: push URL {} for later redirect - now going for the autosignin", path);
				session(Const.REDIRECT_TO_URL, path);//let's be nice and redirect here after authentication
				redirect("/platform/auth/autosignin");
				return;
			}

			if (pathExemptFromAuthentication(path)) {//we don't deal with exempt paths (since we'll enter a loop)
				log.debug("path {} is exempt from auth cookie filter", path);
				return;
			}

			authenticationRequired();
		}
	}

	protected void ensureAuthorized(Partner authenticatedPartner, String path) {
		if (path.startsWith("/platform/statistics") && !authenticatedPartner.getBoolean("can_view_statistics")) {
			notAuthorized();
		}
		if (path.startsWith("/platform/partners") && !authenticatedPartner.getBoolean("can_manage_partners")) {
			notAuthorized();
		}
		// TODO restrict access only to authorized sections

	}

	protected void notAuthorized() {
		flash("access_denied", "not_authorized");
		redirect("/platform/auth");
	}

	protected void authenticationRequired() {
		flash("access_denied", "authentication_required");
		session(Const.REDIRECT_TO_URL, path());//let's be nice and redirect here after authentication
		flash("should_sign_up", "true");//homepage should already show the sign up form...
		redirect("/platform/auth");
	}

	public boolean pathExemptFromAuthentication(String path) {
		if (path.equalsIgnoreCase("/platform/auth")) return true;
		if (path.equalsIgnoreCase("/platform/auth/index")) return true;
		if (path.equalsIgnoreCase("/platform/auth/autosignin")) return true;
		if (path.equalsIgnoreCase("/platform/auth/validate")) return true;
		if (path.equalsIgnoreCase("/platform/auth/reset_password") && method().equalsIgnoreCase("POST")) return true;
		if (path.equalsIgnoreCase("/platform/auth/signin") && method().equalsIgnoreCase("POST")) return true;
		if (path.equalsIgnoreCase("/platform/auth/register") && method().equalsIgnoreCase("POST")) return true;
		return false;
	}

}
