package app.controllers;

import org.javalite.activeweb.ControllerSpec;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeControllerSpec extends ControllerSpec {

	final static Logger log = LoggerFactory.getLogger(HomeControllerSpec.class);

	@Test
	public void basicRoundrip() throws Exception {
		request().get("index");
	}
}
