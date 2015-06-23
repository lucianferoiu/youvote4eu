package app.services;

import app.services.impl.GMailMailer;

import com.google.inject.AbstractModule;

/**
 * Module gathering the communications-related services
 */
public class CommunicationsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Mailer.class).to(GMailMailer.class).asEagerSingleton();
	}

}
