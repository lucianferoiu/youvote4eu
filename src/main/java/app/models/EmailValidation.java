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

	public static EmailValidation findValidation(String code) {
		return EmailValidation.findFirst("token=? and validated=false and is_registration=true and valid_until>=current_timestamp ", code);
	}
}
