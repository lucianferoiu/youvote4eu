package app.models;

import org.javalite.activeweb.DBSpec;
import org.junit.Test;

public class LangSpec extends DBSpec {

	@Test
	public void save() {
		Lang entity = new Lang();
		entity.set("id", 13L).set("code", "fr");
		boolean succ = entity.insert();
		a(succ).shouldBeTrue();
		Lang l1 = Lang.findById(13L);
		a(l1.getId()).shouldNotBeNull();
		a(l1.get("code")).shouldBeEqual("fr");
	}
}
