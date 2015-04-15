package app.services;

public interface MessageDigester {

	/**
	 * Encodes a message with SHA-512 digester and returns a base64 of the
	 * result.
	 */
	String digest(String string);

}
