package app.base;

import org.javalite.activeweb.AppController;

import app.services.TokenGenerator;

import com.google.inject.Inject;

/**
 * Base controller for the Platform (admin) section of the application
 */
public abstract class PlatformController extends AppController {

	@Inject
	protected TokenGenerator tokenGenerator;

	@Override
	protected String getLayout() {
		return "layouts/platform_layout";
	}
}
