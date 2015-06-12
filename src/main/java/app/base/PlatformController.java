package app.base;

import java.sql.Time;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.MetaModel;
import org.javalite.activejdbc.Model;
import org.javalite.activeweb.AppController;
import org.javalite.activeweb.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * - sort uses a prepended \"-\" to denote descending order of sort<br/>
	 * - returns a JSON struct that reports the total number of items too:
	 * {total:.., results: [..], from:.., to:.. }
	 */
	protected void returnJsonResults(MetaModel metamodel, LazyList rows, Long total, String... excludeFields) {

		Long from = 0L;
		Long to = total - 1;
		String sortBy = "id";
		String dir = " asc nulls first ";

		try {
			String fromParam = param("from");
			if (!StringUtils.nullOrEmpty(fromParam)) {
				from = Long.decode(fromParam);
			}

			String toParam = param("to");
			if (!StringUtils.nullOrEmpty(toParam)) {
				to = Long.decode(toParam);
			}
		}
		catch (NumberFormatException nfe) {
			from = 0L;
			to = Math.max(total - 1, 10);
		}

		String sortByParam = param("sort");
		if (!StringUtils.nullOrEmpty(sortByParam)) {
			if (sortByParam.startsWith("-")) {
				dir = " desc nulls last ";
				sortByParam = sortByParam.substring(1);
			}
			if (sortByParam.matches("\\w+")) {//we only accept one word - no cheeky sql injection, sir!
				sortBy = sortByParam;
			} else {
				log.debug("Offensive sort param \"{}\"", sortByParam);
			}
		}

		rows = rows.orderBy(sortBy + dir).offset(from).limit(Math.max(to - from, 0) + 1);

		String json = JsonHelper.toResultsJson(rows, total, from, to, true, //
				metamodel.getAttributeNamesSkip(excludeFields));
		if (json == null) {
			json_404("results not found");
			return;
		}
		respond(json).contentType("application/json").status(200);
	}

	protected void returnJson(MetaModel metaModel, Model model, String... excludeFields) {
		List<String> l = metaModel.getAttributeNamesSkip(excludeFields);
		respond(model.toJson(true, l.toArray(new String[0]))).contentType("application/json").status(200);
	}

	protected void returnJsonList(List results) {
		String json = JsonHelper.toListJson(results);
		respond(json).contentType("application/json").status(200);
	}

	/**
	 * JSON 200 - ok
	 */
	protected void json_200(String msg) {//generic success
		respond("{\"message\":\"" + msg + "\"}").contentType("application/json").status(200);
	}

	/**
	 * JSON 201 - content created
	 */
	protected void json_201(String json) {//new content created - send the content back
		respond(json).contentType("application/json").status(201);
	}

	/**
	 * JSON 204 - success but no new content
	 */
	protected void json_204() {//success but no new content
		respond("").contentType("application/json").status(204);
	}

	/**
	 * JSON 400 - bad request
	 */
	protected void json_400(String msg) {//bad request
		respond("{\"message\":\"" + msg + "\"}").contentType("application/json").status(400);
	}

	/**
	 * JSON 403 - unauthorized
	 */
	protected void json_403() {//unauthorized access
		respond("unauthorized").contentType("application/json").status(204);
	}

	/**
	 * JSON 404 - not found
	 */
	protected void json_404(String msg) {//not found
		respond("{\"message\":\"" + msg + "\"}").contentType("application/json").status(404);
	}

	/**
	 * JSON 500 - server error
	 */
	protected void json_500(String msg) {//server error
		respond("{\"message\":\"" + msg + "\"}").contentType("application/json").status(501);
	}

	protected void parseTimestamps(Model model, Map<String, Object> atts) {
		for (String key : atts.keySet()) {
			Object val = atts.get(key);
			if (val != null) {
				if (key.endsWith("_at") || key.endsWith("_on")) {
					//ZonedDateTime date = ZonedDateTime.parse(val.toString(), DateTimeFormatter.ISO_INSTANT);
					model.setTimestamp(key, new Time((Long) val));
				} else if (val instanceof Map) {//deep parse
					parseTimestamps(model, (Map<String, Object>) val);
				}
			}
		}
	}

}
