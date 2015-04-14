package app.models;

import org.javalite.activeweb.DBSpec;
import org.junit.Test;

public class VoteSpec extends DBSpec {

	@Test
	public void save() {
		Vote entity = new Vote();
		boolean succ = entity.save();
		a(succ).shouldBeFalse();
		entity.set("question_id", 123L);
		succ = entity.save();
		a(succ).shouldBeFalse();
		entity.set("citizen_id", 456L);
		succ = entity.save();
		a(succ).shouldBeFalse();
		entity.set("value", 1);
		succ = entity.save();
		a(succ).shouldBeTrue();

		a(entity.getId()).shouldNotBeNull();
		Vote v1 = Vote.findById(entity.getId());
		a(v1).shouldNotBeNull();
		a(v1.get("question_id")).shouldBeEqual(123L);
		a(v1.get("citizen_id")).shouldBeEqual(456L);
		a(v1.get("value")).shouldBeEqual(1);
	}

}
