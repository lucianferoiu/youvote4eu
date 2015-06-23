package app.config;

import org.javalite.activeweb.AppContext;
import org.javalite.activeweb.Bootstrap;
import org.javalite.activeweb.Configuration;

import app.services.CommunicationsModule;
import app.services.CryptoModule;
import app.services.LogicModule;

import com.google.inject.Guice;

public class AppBootstrap extends Bootstrap {

	@Override
	public void init(AppContext context) {
		Configuration.setUseDefaultLayoutForErrors(false);
		setInjector(Guice.createInjector(new CryptoModule(), new CommunicationsModule(), new LogicModule()));

	}
}
