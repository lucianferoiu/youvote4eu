package app.scenarios.platform;

import org.javalite.activeweb.AppIntegrationSpec;
import org.javalite.activeweb.Cookie;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.Const;
import app.models.Partner;

public class PlatformAccessSpec extends AppIntegrationSpec {

	static final Logger log = LoggerFactory.getLogger(PlatformAccessSpec.class);

	@Test
	public void firstAccessHome() throws Exception {
		Cookie c = cookie(Const.PARTNER_AUTH_COOKIE_NAME);
		a(c).shouldBeNull();
		controller("/platform/home").get("index");
		c = cookie(Const.PARTNER_AUTH_COOKIE_NAME);
		a(c).shouldBeNull();//cookie set only for authenticated users that requested it
		a(sessionObject(Const.AUTHENTICATED_PARTNER)).shouldBeNull();
	}

	@Test
	public void accessRandomPlatformPage() throws Exception {
		Cookie c = cookie(Const.PARTNER_AUTH_COOKIE_NAME);
		a(c).shouldBeNull();
		a(sessionObject(Const.AUTHENTICATED_PARTNER)).shouldBeNull();
		controller("/platform/statistics").get("index");
		it(redirected()).shouldBeTrue();
		a(redirectValue()).shouldBeEqual("/platform/home");
		Cookie initialCookie = cookie(Const.PARTNER_AUTH_COOKIE_NAME);
		a(initialCookie).shouldBeNull();
		a(sessionObject(Const.AUTHENTICATED_PARTNER)).shouldBeNull();
		String errType = (String) flash("access_denied");
		it(errType).shouldNotBeNull();
		it(errType).shouldBeEqual("authentication_required");

	}

	@Test
	public void rememberMeAccessHome() throws Exception {
		//setup
		String tok = "already-logged-in-with-remember-me-cookie";
		Cookie cookie = new Cookie(Const.PARTNER_AUTH_COOKIE_NAME, tok);
		Partner dbPartner = Partner.createIt("email", "registered@example.com", "auth_token", tok, "verified", true,
				"enabled", true);
		//		controller("/platform/home").cookie(cookie);//mock cookies are not preserved across requests, we have to set them explicitly
		a(sessionObject(Const.AUTHENTICATED_PARTNER)).shouldBeNull();
		controller("/platform/home").cookie(cookie).get("home");
		it(redirected()).shouldBeTrue();
		a(redirectValue()).shouldBeEqual("/platform/home/autosignin");
		controller("/platform/home").cookie(cookie).get("autosignin");
		Partner sessionPartner = (Partner) sessionObject(Const.AUTHENTICATED_PARTNER);
		a(sessionPartner).shouldNotBeNull();
		a(sessionPartner.get("email")).shouldBeEqual(dbPartner.get("email"));
	}

}
