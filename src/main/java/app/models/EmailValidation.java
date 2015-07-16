package app.models;

import org.javalite.activejdbc.Model;

/**
 * Persistent authentication token used to track the anonymous citizen
 */
public class EmailValidation extends Model {
	static {
		//		validatePresenceOf("email", "token");
		//		validateEmailOf("email");
	}

	public static EmailValidation findPartnerValidation(String code) {
		return EmailValidation.findFirst(
				"token=? and validated=false and (is_registration=true or is_pwd_renew=true) and valid_until>=current_timestamp ", code);
	}

	public static EmailValidation findCitizenValidation(String code) {
		return EmailValidation.findFirst("token=? and validated=false and is_citizen=true and valid_until>=current_timestamp", code);
	}

}
