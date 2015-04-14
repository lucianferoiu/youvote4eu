package app.config;

import org.javalite.activeweb.AppContext;
import org.javalite.activeweb.Bootstrap;

import app.services.TokenModule;

import com.google.inject.Guice;

public class AppBootstrap extends Bootstrap {

	@Override
	public void init(AppContext context) {
		setInjector(Guice.createInjector(new TokenModule()));
	}
}
