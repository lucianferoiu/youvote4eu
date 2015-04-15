package app.services;

import app.services.impl.SecureMessageDigester;
import app.services.impl.SecureRandomTokenGenerator;

import com.google.inject.AbstractModule;

/**
 * Module gathering the cryptography-related services
 */
public class CryptoModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TokenGenerator.class).to(SecureRandomTokenGenerator.class).asEagerSingleton();
		bind(MessageDigester.class).to(SecureMessageDigester.class).asEagerSingleton();
	}

}
