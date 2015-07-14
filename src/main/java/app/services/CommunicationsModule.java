package app.services;

import app.services.impl.GMailMailer;

import com.google.inject.AbstractModule;

/**
 * Module gathering the communications-related services
 */
public class CommunicationsModule extends AbstractModule {

	@Override
	protected void configure() {

		String deploymentEnv = System.getenv("DEPLOY_ENV");
		if ("heroku".equalsIgnoreCase(deploymentEnv)) {
			bind(Mailer.class).to(GMailMailer.class).asEagerSingleton();
		} else {
			bind(Mailer.class).to(GMailMailer.class).asEagerSingleton();
		}
	}

}
