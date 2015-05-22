package app.base;

import org.javalite.activeweb.AppController;
import org.javalite.activeweb.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.services.Mailer;
import app.services.MessageDigester;
import app.services.TokenGenerator;

import com.google.inject.Inject;

/**
 * Base controller for the Platform (admin) section of the application
 */
public abstract class PlatformController extends AppController {

	static final Logger log = LoggerFactory.getLogger(PlatformController.class);

	@Inject
	protected TokenGenerator tokenGenerator;

	@Inject
	protected MessageDigester messageDigester;

	@Inject
	protected Mailer mailer;

	@Override
	protected String getLayout() {
		return "platform/layout";
	}

	protected String hostname() {
		String hname = url().substring(0, url().indexOf(Const.PLATFORM_PATH));
		return hname;
	}

	protected void resetAuthCookie() {
		Cookie c = cookie(Const.PARTNER_AUTH_COOKIE_NAME);
		if (c != null) {
			c.setValue("deleted");
			//c.setSecure(true); // TODO: uncomment this after setting up the SSL
			c.setPath("/platform");
			c.setVersion(1);
			c.setMaxAge(0);//reset
			log.debug("Resetting authorization cookie {}", c.toString());
			sendCookie(c);
		}
	}

	protected void setAuthCookie(String token) {
		Cookie c = new Cookie(Const.PARTNER_AUTH_COOKIE_NAME, token, true);
		c.setMaxAge(60 * 60 * 24 * 14);//valid for two weeks
		c.setVersion(1);
		//c.setSecure(true); // TODO: uncomment this after setting up the SSL
		c.setPath("/platform");
		log.debug("Setting authorized Partner cookie {}", c.toString());
		sendCookie(c);
	}

}
