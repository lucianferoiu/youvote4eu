package app.services.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.javalite.common.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.services.MessageDigester;
import app.util.StringUtils;

public class SecureMessageDigester implements MessageDigester {

	static final Logger log = LoggerFactory.getLogger(SecureMessageDigester.class);

	@Override
	public String digest(String message) {
		if (StringUtils.nullOrEmpty(message)) return null;
		byte[] messageBytes = null;
		try {
			messageBytes = message.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e1) {
			log.warn("Cannot decode message {} as UTF-8", message);
			messageBytes = message.getBytes();
		}

		try {
			MessageDigest digester = MessageDigest.getInstance("SHA-512");
			byte[] digest = digester.digest(messageBytes);
			if (digest != null) {
				byte[] base64digest = Base64.getEncoder().encode(digest);
				return new String(base64digest);
			}
		}
		catch (NoSuchAlgorithmException e) {
			log.error("Cannot use the SHA-512 algorithm for the message digester", e);
		}

		return null;
	}

}
