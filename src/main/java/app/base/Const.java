package app.base;

import java.util.regex.Pattern;

/**
 * Set of application-wide constants
 */
public interface Const {
	static final String APP_VER = "0.1";
	static final String AUTH_COOKIE_NAME = "YV4EU";
	static final String PARTNER_AUTH_COOKIE_NAME = "PPYV4EU";
	static final String PLATFORM_PATH = "/platform/";
	static final String AUTHENTICATED_PARTNER = "authPartner";
	static final String REDIRECT_TO_URL = "redirect-to-url";
	static final String EMAIL_REGEXP = "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}";
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
			"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

}
