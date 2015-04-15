package app.services.impl;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.services.TokenGenerator;

public class SecureRandomTokenGenerator implements TokenGenerator {

	static final Logger log = LoggerFactory.getLogger(SecureRandomTokenGenerator.class);

	private final SecureRandom random = new SecureRandom();

	public SecureRandomTokenGenerator() {
		log.info("Initializing SecureRandom for the token generator");

		//I know the SecureRandom seeds itself on the first run, but hey! paranoia is good company to security
		generateToken();
		int howMany = 3 + (random.nextInt() % 23);
		for (int i = 0; i < howMany; i++) {
			generateToken();
		}
	}

	@Override
	public String generateToken() {
		return new BigInteger(180, random).toString(32);
	}

}
