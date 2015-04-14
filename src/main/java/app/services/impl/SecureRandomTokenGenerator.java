package app.services.impl;

import java.math.BigInteger;
import java.security.SecureRandom;

import app.services.TokenGenerator;

public class SecureRandomTokenGenerator implements TokenGenerator {

	private final SecureRandom random = new SecureRandom();

	@Override
	public String generateToken() {
		return new BigInteger(180, random).toString(32);
	}

}
