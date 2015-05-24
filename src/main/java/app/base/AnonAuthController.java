package app.base;

import org.javalite.activeweb.AppController;

import app.services.TokenGenerator;

import com.google.inject.Inject;

public abstract class AnonAuthController extends AppController {

	@Inject
	protected TokenGenerator tokenGenerator;

	@Override
	protected String getLayout() {
		return "layout";
	}
}