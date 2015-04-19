package app.models;

import org.javalite.activejdbc.Model;

/**
 * Persistent authentication token used to track the anonymous citizen
 */
public class EmailValidation extends Model {
	static {
		validatePresenceOf("email", "token");
		validateEmailOf("email");
	}
}
