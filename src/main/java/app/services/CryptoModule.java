package app.services;

import app.services.impl.SecureRandomTokenGenerator;

import com.google.inject.AbstractModule;

public class TokenModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TokenGenerator.class).to(SecureRandomTokenGenerator.class).asEagerSingleton();
	}

}
