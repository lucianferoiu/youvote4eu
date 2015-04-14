package app.models;

import org.javalite.activeweb.DBSpec;
import org.junit.Test;

public class TagSpec extends DBSpec {

	@Test
	public void save() {
		Tag entity = new Tag();
		entity.set("id", 13);
		boolean succ = entity.insert();
		a(succ).shouldBeTrue();
		Tag t1 = Tag.findById(13);
		a(t1.getLongId()).shouldBeEqual(13);
	}
}
