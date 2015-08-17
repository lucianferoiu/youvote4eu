package app.controllers.platform;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.javalite.activeweb.Configuration;
import org.javalite.activeweb.annotations.GET;
import org.javalite.activeweb.annotations.POST;
import org.javalite.activeweb.annotations.PUT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.Const;
import app.base.PlatformController;
import app.models.EmailValidation;
import app.models.Partner;
import app.util.JsonHelper;
import app.util.StringUtils;

public class PartnersController extends PlatformController {

	private static final Logger log = LoggerFactory.getLogger(PartnersController.class);
	protected static final int PAGE_SIZE = 10;
	protected static final String[] EXCLUDED_FIELDS = { "password", "auth_token", "created_at", "updated_at" };

	/**
	 * Render AngularJS SPA (the rest of the methods are JSON-based)
	 * 
	 * @see /views/platform/partners/index.ftl
	 * @see /app/platform/partners/*.js etc.
	 */
	public void index() {}

	@GET
	public void list() {
		returnJsonResults(Partner.getMetaModel(), Partner.findAll(), Partner.count(), EXCLUDED_FIELDS);
	}

	@GET
	public void edit() {
		String idParam = param("id");
		try {
			if (!StringUtils.nullOrEmpty(idParam)) {
				Long partnerId = Long.decode(idParam);
				Partner partner = Partner.findById(partnerId);
				log.trace("Editing partner with id={}", partnerId);
				returnJson(Partner.getMetaModel(), partner, EXCLUDED_FIELDS);
			}
		}
		catch (NumberFormatException nfe) {
			json_400("invalid partner id: " + idParam);
		}

	}

	@POST
	public void save() {
		Map atts = Collections.emptyMap();
		Long id = null;

		try {
			atts = JsonHelper.toMap(getRequestString());
			if (atts != null && atts.containsKey("id")) {
				id = Long.decode(atts.get("id").toString());
			}
		}
		catch (IOException e) {
			log.warn("Cannot save partner.. POST payload corrupted");
			json_400("Cannot read POST payload");
		}
		catch (NumberFormatException nfe) {
			log.warn("Cannot save partner - malformed id={}", atts.get("id"));
			json_400("Malformed partner id");
		}

		if (atts == null) json_400("Cannot read POST payload");

		Partner partner = new Partner();
		if (id != null) {//update
			partner = Partner.findById(id);
			if (partner == null) {
				log.debug("Cannot find a partner with id={}", id);
				json_404(String.format("No existing partner with id: %d", id));
			}
		}
		atts.remove("first_login");
		atts.remove("last_login");
		atts.remove("email");//prevent a smarty-pants from manipulating the POST payload and changing the email
		partner.fromMap(atts);
		boolean succ = partner.save();
		if (succ) {
			if (id == null) {//was new content
				sendVerificationMailToNewPartner(partner.getString("email"), "/other/mail/added_partner");
				json_201(String.format("new partner with id: %d", partner.getLongId()));
			} else {
				json_200(String.format("updated partner with id: %d", partner.getLongId()));
			}
		} else {
			log.warn("Cannot save partner with id={} - db errors: {}", id, partner.errors().toString());
			json_400("cannot update partner details");
		}

	}

	@PUT
	public void add() {
		redirect("/platform/partners/list");
	}

	@POST
	public void ban() {
		Map atts = Collections.emptyMap();
		Long id = null;

		try {
			atts = JsonHelper.toMap(getRequestString());
			if (atts != null && atts.containsKey("id")) {
				id = Long.decode(atts.get("id").toString());
			}
		}
		catch (IOException e) {
			log.warn("Cannot ban partner.. POST payload corrupted");

		}
		catch (NumberFormatException nfe) {
			log.warn("Cannot bsn partner - malformed id={}", atts.get("id"));
			json_400("Malformed partner id");
		}

		if (atts == null) json_400("Cannot read POST payload");

		Partner partner = new Partner();
		if (id != null) {
			partner = Partner.findById(id);
			if (partner == null) {
				log.debug("Cannot find a partner with id={}", id);
				json_404(String.format("No existing partner with id: %d", id));
				return;
			}
		} else {
			log.debug("Missing partner id - don't know who to ban");
			json_404(String.format("Missing partner id"));
			return;

		}

		Partner authenticatedPartner = (Partner) session(Const.AUTHENTICATED_PARTNER);
		if (authenticatedPartner == null) {
			log.debug("The ban initiator cannot be annonymous - how did you access this anyway!?", id);
			json_403();
			return;

		}

		String message = atts.containsKey("message") ? (String) atts.get("message")
				: "Your activity as Platform Partner on YouVoteForEurope has been restricted.";

		partner.setBoolean("enabled", false);
		partner.setDate("banned_at", new Date());
		partner.setDate("banned_by", authenticatedPartner.getLongId());
		partner.setString("ban_reason", message);
		boolean succ = partner.save();
		if (succ) {
			sendMessageToPartner(partner.getString("email"), "/other/mail/message_partner", "Your access has been restricted", message);
			json_200(String.format("banned partner with id: %d", partner.getLongId()));
		} else {
			log.warn("Cannot save partner with id={} - db errors: {}", id, partner.errors().toString());
			json_400("cannot update (ban) partner details");
		}

	}

	public void message() {

	}

	protected void sendVerificationMailToNewPartner(String email, String template) {
		String validationCode = tokenGenerator.generateToken().trim();
		String url = hostname() + "/platform/auth/validate?code=" + validationCode;
		String subj = "You have been endorsed as YouVoteForEurope Partner";
		Map<String, Object> vals = new HashMap<String, Object>();
		vals.put("url", url);

		StringWriter writer = new StringWriter();
		Configuration.getTemplateManager().merge(vals, template, null, null, writer);

		LocalDateTime now = LocalDateTime.now();
		Date tomorrow = Date.from(now.plusHours(24).toInstant(ZoneOffset.UTC));
		EmailValidation validation = EmailValidation.create("email", email, "token", validationCode, "validated", false);
		validation.setTimestamp("valid_until", tomorrow).saveIt();
		Partner me = (Partner) session(Const.AUTHENTICATED_PARTNER);
		if (me == null) return;
		validation.setLong("added_by", me.getId());

		mailer.sendMail(email, subj, writer.toString(), true);
		log.debug("Partner added: created email validation {} and sent the validation email for URL {}", validation, url);
	}

	protected void sendMessageToPartner(String email, String template, String subject, String message) {
		Map<String, Object> vals = new HashMap<String, Object>();
		vals.put("url", hostname() + "/platform");
		vals.put("message", message);

		StringWriter writer = new StringWriter();
		Configuration.getTemplateManager().merge(vals, template, null, null, writer);
		mailer.sendMail(email, subject, writer.toString(), true);
		log.debug("Sent email '{}' to partner {}", subject, email);

	}

}
