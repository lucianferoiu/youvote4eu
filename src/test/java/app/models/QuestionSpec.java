package app.models;

import org.javalite.activeweb.DBSpec;
import org.junit.Test;

public class QuestionSpec extends DBSpec {

	@Test
	public void save() {
		Question entity = new Question();
		boolean succ = entity.save();
		a(succ).shouldBeTrue();
		a(entity.getLongId()).shouldNotBeNull();
	}

}
