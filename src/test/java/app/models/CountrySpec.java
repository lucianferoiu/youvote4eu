package app.models;

import org.javalite.activeweb.DBSpec;
import org.junit.Test;

public class CountrySpec extends DBSpec {
	@Test
	public void save() {
		Country entity = new Country();
		entity.setId(13L).set("code", "fi");
		boolean succ = entity.insert();
		a(succ).shouldBeTrue();
		a(entity.getId()).shouldNotBeNull();
		Country c1 = Country.findById(13L);
		a(c1.getId()).shouldNotBeNull();
		a(c1.get("code")).shouldBeEqual("fi");
	}

}
