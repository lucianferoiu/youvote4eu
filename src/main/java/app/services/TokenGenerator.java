package app.services;

/**
 * Provides on-demand random strings that are cryptographically strong (i.e.
 * extremely low chance of repetition) to be used as authentication tokens
 * (cookies).
 */
public interface TokenGenerator {

	/**
	 * Generates a random string of ~36 characters long
	 */
	public String generateToken();

}
