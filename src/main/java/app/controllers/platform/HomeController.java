package app.controllers.platform;

import org.javalite.activeweb.Cookie;

import app.base.Const;
import app.base.PlatformController;
import app.models.Partner;

/**
 * Homepage of the platform (admin) section
 */
public class HomeController extends PlatformController {

	public void index() {}

	public void autosignin() {
		Cookie authToken = cookie(Const.PARTNER_AUTH_COOKIE_NAME);
		if (authToken == null) {//no "remember me" cookie
			flash("access_denied", "missing_cookie");
			//flash("should_sign_up", "true");//homepage should already show the sign up form...
			redirect("/platform/home");
		} else {
			Partner partnerInDB = Partner.findByAuthToken(authToken.getValue());
			if (partnerInDB == null) {
				flash("access_denied", "wrong_cookie");

			} else {
				if (!partnerInDB.getBoolean("verified")) {
					flash("access_denied", "not_verified");
					redirect("/platform/home");
				} else if (!partnerInDB.getBoolean("enabled")) {
					flash("access_denied", "account_disabled");
					redirect("/platform/home");
				} else {//finally, all's well
					session(Const.AUTHENTICATED_PARTNER, partnerInDB);
					if (sessionHas(Const.REDIRECT_TO_URL)) {
						String redirectUrl = sessionString(Const.REDIRECT_TO_URL);
						session(Const.REDIRECT_TO_URL, null);
						redirect(redirectUrl);
					} else {
						redirect("/platform/home");
					}
				}
			}

		}

	}

}
