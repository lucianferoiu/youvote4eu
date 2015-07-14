package app.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.services.impl.GMailMailer;
import app.services.impl.SendGridMailer;

import com.google.inject.AbstractModule;

/**
 * Module gathering the communications-related services
 */
public class CommunicationsModule extends AbstractModule {

	static final Logger log = LoggerFactory.getLogger(CommunicationsModule.class);

	@Override
	protected void configure() {

		String deploymentEnv = System.getenv("DEPLOY_ENV");
		if ("heroku".equalsIgnoreCase(deploymentEnv)) {
			log.debug("Setting the Mailer service to [SendGridMailer]");
			bind(Mailer.class).to(SendGridMailer.class).asEagerSingleton();
		} else {
			log.debug("Setting the Mailer service to [GMailMailer]");
			bind(Mailer.class).to(GMailMailer.class).asEagerSingleton();
		}
	}

}
