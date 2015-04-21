package app.controllers.platform;

import org.javalite.activeweb.Cookie;
import org.javalite.activeweb.DBControllerSpec;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.Const;

public class HomeControllerSpec extends DBControllerSpec {

	final static Logger log = LoggerFactory.getLogger(HomeControllerSpec.class);

	@Test
	public void firstAccessHome() throws Exception {
		Cookie c = cookie(Const.PARTNER_AUTH_COOKIE_NAME);
		a(c).shouldBeNull();
		request().get("index");
		c = cookie(Const.PARTNER_AUTH_COOKIE_NAME);
		//		a(c).shouldNotBeNull(); - we need filters... so basically we need to setup an AppIntegrationSpec scenario for all this..
	}

}
