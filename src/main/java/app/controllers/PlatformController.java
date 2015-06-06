package app.controllers;

import app.base.AnonAuthController;

/**
 * Redirects to the platform homepage
 */
public class PlatformController extends AnonAuthController {

	public void index() {
		redirect("/platfom/home");
	}

}
