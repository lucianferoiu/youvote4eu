package app.models;

import org.javalite.activejdbc.Model;

/**
 * The platform partners, users capable of managing the content of the
 * application
 */
public class Partner extends Model {
	static {
		validatePresenceOf("email");
		validateEmailOf("email");
	}

	public boolean canAuthenticate() {
		return getBoolean("enabled") && getBoolean("verified");
	}

	public static Partner findByAuthToken(String value) {
		return Partner.findFirst("auth_token=?", value);
	}

}
