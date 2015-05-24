package app.base;

import org.javalite.activejdbc.LazyList;
import org.javalite.activeweb.AppController;
import org.javalite.activeweb.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.models.Partner;
import app.services.Mailer;
import app.services.MessageDigester;
import app.services.TokenGenerator;
import app.util.JsonHelper;
import app.util.StringUtils;

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

	/**
	 * Handy method for returning a paginated resultset in JSON format. Ensures
	 * the uniformity and conformity to convention: <br/>
	 * - expected GET query parameters: from, to and orderBy <br/>
	 * - orderBy uses a prepended '-' to denote descending order of sort<br/>
	 * - returns a JSON struct that reports the total number of items too:
	 * {total:.., results: [..], from:.., to:.. }
	 */
	protected void returnJsonResults(LazyList<Partner> rows, Long total, String... excludeFields) {

		Long from = 0L;
		Long to = total - 1;
		String orderBy = "first_login";
		String dir = " asc ";

		try {
			String fromParam = param("from");
			if (!StringUtils.nullOrEmpty(fromParam)) {
				from = Long.decode(fromParam);
			}
			String toParam = param("to");
			if (!StringUtils.nullOrEmpty(toParam)) {
				to = Long.decode(toParam);
			}
			String orderByParam = param("orderBy");
			if (!StringUtils.nullOrEmpty(orderByParam)) {
				if (orderByParam.startsWith("-")) {
					dir = " desc ";
					orderByParam = orderByParam.substring(1);
					if (orderByParam.matches("\\w")) {//we only accept one word - no cheeky sql injection, sir!
						orderBy = orderByParam;
					} else {
						log.debug("Offensive orderBy param '%s'", orderByParam);
					}
				}
			}
		}
		catch (NumberFormatException nfe) {
			from = 0L;
			to = total - 1;
		}

		rows = rows.offset(from).limit(Math.max(to - from, 0) + 1).orderBy(orderBy + dir);

		String json = JsonHelper.toResultsJson(rows, total, from, to, true, //
				Partner.getMetaModel().getAttributeNamesSkip(excludeFields));
		if (json == null) {
			respond("\"results not found\"").contentType("application/json").status(404);
			return;
		}
		respond(json).contentType("application/json").status(200);
	}

}
