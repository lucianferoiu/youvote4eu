package app.scenarios.platform;

import org.javalite.activeweb.AppIntegrationSpec;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartnerRegistrationSpec extends AppIntegrationSpec {

	static final Logger log = LoggerFactory.getLogger(PartnerRegistrationSpec.class);

	@Test
	public void dummy() {
		controller("/platform/auth").post("register");
	}

}
