package app.config;

import org.javalite.activeweb.AppContext;
import org.javalite.activeweb.Bootstrap;
import org.javalite.activeweb.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.services.CommunicationsModule;
import app.services.CryptoModule;
import app.services.LogicModule;

import com.google.inject.Guice;

public class AppBootstrap extends Bootstrap {

	static final Logger log = LoggerFactory.getLogger(AppBootstrap.class);

	@Override
	public void init(AppContext context) {

		log.warn("Deployment environment is {}", System.getenv("DEPLOY_ENV"));

		Configuration.setUseDefaultLayoutForErrors(false);
		setInjector(Guice.createInjector(new CryptoModule(), new CommunicationsModule(), new LogicModule()));

	}
}
