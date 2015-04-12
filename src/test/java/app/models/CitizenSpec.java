package app.models;

import org.javalite.activeweb.DBSpec;
import org.junit.Test;

public class CitizenSpec extends DBSpec {

	@Test
	public void dummyRecordValidation() {
		Citizen citizen = new Citizen();
		a(citizen).shouldBe("valid");
		a(citizen.get("validated")).shouldBeNull();
		boolean saved = citizen.saveIt();
		// a(citizen.get("validated")).shouldBeFalse(); <-- it seems that PostgreSQL sets the defaults upon commit, which doesn't happen in a DBSpec (a rollback is issued)
		a(saved).shouldBeTrue();
	}

}
