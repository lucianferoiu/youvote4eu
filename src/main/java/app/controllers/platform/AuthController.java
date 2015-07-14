package app.controllers.platform;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.javalite.activeweb.Configuration;
import org.javalite.activeweb.Cookie;
import org.javalite.activeweb.annotations.GET;
import org.javalite.activeweb.annotations.POST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.Const;
import app.base.PlatformController;
import app.models.EmailValidation;
import app.models.Partner;
import app.util.StringUtils;

/**
 * Support for registration, validation and signing in (both explicitly and
 * through cookie)
 */

public class AuthController extends PlatformController {

	public static final Logger log = LoggerFactory.getLogger(PlatformController.class);

	@Override
	protected String getLayout() {
		return "platform/auth/layout";
	}

	public void index() {
		if (sessionHas(Const.AUTHENTICATED_PARTNER)) {//we're authenticated; the controller has done its duty
			if (sessionHas(Const.REDIRECT_TO_URL)) {
				String redirectUrl = sessionString(Const.REDIRECT_TO_URL);
				session(Const.REDIRECT_TO_URL, null);
				log.debug("Redirecting to stack URL {}", redirectUrl);
				redirect(redirectUrl);
			} else {
				redirect("/platform/home");
			}
		}
	}

	@POST
	public void signin() {
		String email = param("pp-signin-email");
		String pwd = param("pp-signin-password");
		Boolean rememberMe = ("on".equalsIgnoreCase(param("pp-signin-remember")));
		if (StringUtils.nullOrEmpty(pwd) || StringUtils.nullOrEmpty(email)) {
			flash("signin_failure", "wrong_params");
			redirect("/platform/auth");
			return;
		}
		email = email.trim().toLowerCase();//sanitize the email address
		Partner partner = Partner.findByEmail(email);
		if (partner == null) {
			log.debug("Cannot find a Partner with the email {}", email);
			flash("signin_failure", "wrong_params");
			redirect("/platform/auth");
			return;
		}

		String shaPwd = messageDigester.digest(pwd, false);
		if (!(shaPwd.endsWith(partner.getString("password")))) {
			log.debug("Partner with email {} has failed to present the proper password (SHA) {}", email, shaPwd);
			flash("signin_failure", "wrong_params");
			redirect("/platform/auth");
			return;
		}

		if (!partner.getBoolean("verified")) {
			log.debug("Partner with email {} trying to sign in is not yet verified", email);
			flash("access_denied", "not_verified");
			redirect("/platform/auth");
			return;
		}

		if (!partner.getBoolean("enabled")) {
			log.debug("Partner with email {} trying to sign in is disabled ", email);
			flash("access_denied", "account_disabled");
			redirect("/platform/auth");
			return;
		}

		//we're all good
		if (rememberMe) {
			String token = tokenGenerator.generateToken();
			setAuthCookie(token);
			partner.set("auth_token", token);
		}

		Date now = new Date();
		if (partner.getDate("first_login") == null) {
			partner.setTimestamp("first_login", now);
		}
		partner.setTimestamp("last_login", now);

		//finalize
		partner.saveIt();
		session(Const.AUTHENTICATED_PARTNER, partner);
		if (sessionHas(Const.REDIRECT_TO_URL)) {
			String redirectUrl = sessionString(Const.REDIRECT_TO_URL);
			session(Const.REDIRECT_TO_URL, null);
			log.debug("Redirecting to stack URL {}", redirectUrl);
			redirect(redirectUrl);
		} else {
			redirect("/platform/auth");
		}
	}

	public void signout() {

		resetAuthCookie();

		Partner partner = (Partner) session(Const.AUTHENTICATED_PARTNER);
		if (partner != null) {
			partner.set("auth_token", null);
			partner.saveIt();
		}

		session(Const.AUTHENTICATED_PARTNER, null);
		session().destroy();
		redirect("/");
	}

	@POST
	public void register() {
		String email = param("pp-register-email");
		String pwd = param("pp-register-password");
		String agree = param("pp-register-agreement");
		if (StringUtils.nullOrEmpty(pwd) || StringUtils.nullOrEmpty(email) || (!"on".equalsIgnoreCase(agree)) || pwd.length() < 7
				|| (!Const.VALID_EMAIL_ADDRESS_REGEX.matcher(email).find())) {
			flash("registration_failure", "wrong_params");
			redirect("/platform/auth");
			return;
		}
		email = email.trim().toLowerCase();//sanitize the email address

		Partner alreadyExisting = Partner.findByEmail(email);
		if (alreadyExisting != null) {
			log.debug("Partner already registered {}", alreadyExisting);
			flash("registration_failure", "duplicate_email");
			redirect("/platform/auth");
			return;
		}

		//we're good to register the new partner
		String shaPwd = messageDigester.digest(pwd, true);
		Partner.createIt("email", email, "password", shaPwd, "verified", false, "enabled", true);
		sendVerificationMail(email, "/other/mail/validation", "is_registration");
		flash("success_message", "registration_successful");
		redirect("/platform/auth");
	}

	@POST
	public void setPassword() {
		String email = param("pp-set-email");
		String code = param("pp-set-code");
		String pwd = param("pp-set-password");
		String agree = param("pp-set-agreement");
		if (StringUtils.nullOrEmpty(pwd) || StringUtils.nullOrEmpty(email) || StringUtils.nullOrEmpty(code)
				|| (!"on".equalsIgnoreCase(agree)) || pwd.length() < 7 || (!Const.VALID_EMAIL_ADDRESS_REGEX.matcher(email).find())) {
			flash("pwd_set_failure", "wrong_params");
			redirect("/platform/auth");
			return;
		}
		email = email.trim().toLowerCase();//sanitize the email address

		Partner partner = Partner.findByEmail(email);
		if (partner == null) {
			log.debug("Partner with email {} doesn't exist", email);
			flash("pwd_set_failure", "email_not_registered");
			redirect("/platform/auth");
			return;
		}

		EmailValidation validation = EmailValidation.findValidation(code);
		if (validation == null || !(email.equalsIgnoreCase(validation.getString("email")))) {
			log.debug("Validation code not paired with email {}", email);
			flash("pwd_set_failure", "wrong_params");
			redirect("/platform/auth");
			return;
		}

		//we're good to register the new partner
		String shaPwd = messageDigester.digest(pwd, true);
		partner.set("password", shaPwd, "verified", true, "enabled", true);
		partner.save();
		validation.set("verified", true);
		validation.save();

		flash("success_message", "added_partner_verified");
		redirect("/platform/auth");
	}

	@GET
	public void validate() {
		String code = param("code");
		if (StringUtils.nullOrEmpty(code)) {
			flash("registration_failure", "validation_code_error");
			redirect("/platform/auth");
			return;
		}

		code = code.trim();
		EmailValidation validation = EmailValidation.findValidation(code);
		if (validation == null) {//no validation or expired
			log.warn("Email validation error: cannot find one for code {}", code);
			flash("registration_failure", "validation_error");
			redirect("/platform/auth");
			return;
		}

		//all's well
		String email = validation.getString("email");
		Partner partner = Partner.findByEmail(email);
		if (partner == null) {
			log.debug("No partner is registered by email {}", email);
			flash("pwd_reset_failure", "nonexisting");
			redirect("/platform/auth");
			return;
		}

		if (validation.get("added_by") != null) {
			//the process finishes in the setPassword for validations of this kind...
		} else {
			validation.set("validated", true);
			partner.set("verified", true);
		}
		validation.save();
		//validation.delete();
		partner.save();
		log.debug("Partner registration finalized: validated new partner {}", partner);
		if (validation.getBoolean("is_pwd_renew")) {//this is not this partner's first dance
			flash("success_message", "new_pwd_verified");
		} else if (validation.getBoolean("is_registration")) {
			flash("success_message", "verification_successful");
		} else if (validation.get("added_by") != null) {
			flash("should_show_set_pwd", "true");
			flash("validation_code", code);
			flash("email", email);
		}
		redirect("/platform/auth");

	}

	public void autosignin() {
		Cookie authToken = cookie(Const.PARTNER_AUTH_COOKIE_NAME);
		if (authToken == null) {//no "remember me" cookie
			log.debug("Weird... we ended up in autosignin but no cookie present...");
			flash("access_denied", "missing_cookie");
			//flash("should_sign_up", "true");//homepage should already show the sign up form...
			redirect("/platform/auth");
		} else {
			Partner partnerInDB = Partner.findByAuthToken(authToken.getValue());
			if (partnerInDB == null) {
				log.warn("No Partner found for cookie {}", authToken);
				resetAuthCookie();
				flash("access_denied", "wrong_cookie");
				// render("/platform/auth/index");
				redirect("/platform/auth");
			} else {
				if (!partnerInDB.getBoolean("verified")) {
					flash("access_denied", "not_verified");
					redirect("/platform/auth");
				} else if (!partnerInDB.getBoolean("enabled")) {
					flash("access_denied", "account_disabled");
					redirect("/platform/auth");
				} else {//finally, all's well
					partnerInDB.setTimestamp("last_login", new Date());
					partnerInDB.saveIt();
					session(Const.AUTHENTICATED_PARTNER, partnerInDB);
					log.debug("Auto signed in Partner {}", partnerInDB.get("email"));
					if (sessionHas(Const.REDIRECT_TO_URL)) {
						String redirectUrl = sessionString(Const.REDIRECT_TO_URL);
						session(Const.REDIRECT_TO_URL, null);
						redirect(redirectUrl);
					} else {
						redirect("/platform/auth");
					}
				}
			}

		}
	}

	@POST
	public void resetPassword() {
		String email = param("pp-reset-email");
		String pwd = param("pp-reset-password");
		if (StringUtils.nullOrEmpty(pwd) || StringUtils.nullOrEmpty(email) || pwd.length() < 7) {
			flash("pwd_reset_failure", "wrong_params");
			redirect("/platform/auth");
			return;
		}
		email = email.trim().toLowerCase();//sanitize the email address

		Partner alreadyExisting = Partner.findByEmail(email);
		if (alreadyExisting == null) {
			log.debug("Partner is not registered by email {}", email);
			flash("pwd_reset_failure", "nonexisting");
			redirect("/platform/auth");
			return;
		}

		String shaPwd = messageDigester.digest(pwd, true);
		alreadyExisting.set("password", shaPwd).set("verified", false).saveIt();
		sendVerificationMail(email, "/other/mail/reset_password", "is_pwd_renew");
		flash("success_message", "pwd_reset_successful");
		redirect("/platform/auth");

	}

	protected void sendVerificationMail(String email, String template, String field) {
		String validationCode = tokenGenerator.generateToken().trim();
		String url = hostname() + "/platform/auth/validate?code=" + validationCode;
		String subj = "Validate your email so you may access YouVoteForEurope";
		Map<String, Object> vals = new HashMap<String, Object>();
		vals.put("url", url);

		StringWriter writer = new StringWriter();
		Configuration.getTemplateManager().merge(vals, template, null, null, writer);

		LocalDateTime now = LocalDateTime.now();
		Date tomorrow = Date.from(now.plusHours(24).toInstant(ZoneOffset.UTC));
		EmailValidation validation = EmailValidation.create("email", email, "token", validationCode, "validated", false);
		validation.setTimestamp("valid_until", tomorrow);
		validation.setBoolean(field, true);//the kind of email verification
		validation.saveIt();

		mailer.sendMail(email, subj, writer.toString(), true);
		log.debug("Partner registration: created email validation {} and sent the validation email for URL {}", validation, url);
	}

}
