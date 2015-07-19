package app.controllers.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.PlatformController;

/**
 * Homepage of the platform (admin) section. <br/>
 */
public class HomeController extends PlatformController {

	public static final Logger log = LoggerFactory.getLogger(HomeController.class);

	public void index() {}

	public void help() {}

	public void about() {}

	public void catchall() {
		log.trace("Redirecting home from {}", uri());
		redirect("/platform/home");
	}
}
