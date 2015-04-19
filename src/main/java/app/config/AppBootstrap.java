package app.config;

import org.javalite.activeweb.AppContext;
import org.javalite.activeweb.Bootstrap;

import app.services.CommModule;
import app.services.CryptoModule;

import com.google.inject.Guice;

public class AppBootstrap extends Bootstrap {

	@Override
	public void init(AppContext context) {
		setInjector(Guice.createInjector(new CryptoModule()));
		setInjector(Guice.createInjector(new CommModule()));
	}
}
