package app.models;

import org.javalite.activeweb.DBSpec;
import org.junit.Test;

public class LabelSpec extends DBSpec {
	@Test
	public void save() {
		Label entity = new Label(123L, "ro");
		boolean succ = entity.save();
		Long id = entity.getLongId();
		a(succ).shouldBeTrue();
		a(entity.getLongId()).shouldNotBeNull();

		Label l1 = Label.findById(id);
		a(l1.get("lang")).shouldBeEqual("ro");
		a(l1.get("label_group")).shouldBeEqual(123L);
	}

	@Test
	public void saveLabelNew() {
		Label label = null;
		label = Label.saveLabel(null, 123L, "¿Quien sabe?");
		a(label).shouldBeNull();
		label = Label.saveLabel("es", null, "¿Quien sabe?");
		a(label).shouldNotBeNull();
		a(label.getGroup()).shouldNotBeNull();
		label = Label.saveLabel("es", 123L, "¿Quien sabe?");
		a(label).shouldNotBeNull();
		a(label.getGroup()).shouldBeEqual(123L);
	}

	@Test
	public void updateLabel() {
		//setup
		Label label = null;
		label = Label.saveLabel("it", 1234L, "Traduttore, traditore");
		a(label).shouldNotBeNull();
		label = Label.saveLabel("it", 1234L, "Che sarà sarà");
		a(label).shouldNotBeNull();
		a(label.getGroup()).shouldBeEqual(1234L);
		a(label.get("lang")).shouldBeEqual("it");
		a(label.get("text")).shouldBeEqual("Che sarà sarà");
		Label sameGroupLabel = Label.saveLabel("sw", 1234L, "vad kommer härnäst");
		a(sameGroupLabel).shouldNotBeNull();
		a(sameGroupLabel.getGroup()).shouldBeEqual(1234L);
		a(sameGroupLabel.getLongId()).shouldNotBeEqual(label.getLongId());
		Label label2 = Label.findById(label.getId());
		a(label2.get("lang")).shouldBeEqual(label.get("lang"));
		a(label2.get("text")).shouldBeEqual(label.get("text"));

	}

}
