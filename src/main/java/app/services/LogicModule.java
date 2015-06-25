package app.services;

import app.services.impl.ImperfectSquaresLayouter;

import com.google.inject.AbstractModule;

/**
 * Module gathering the algorithms-related services
 */
public class LogicModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(QuestionsLayouter.class).to(ImperfectSquaresLayouter.class).asEagerSingleton();
	}

}
