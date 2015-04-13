package app.models;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

public class Label extends Model {
	static {
		Model.validatePresenceOf("lang");
	}

	private Label() {
		//forbid creation of the label with no language
	}

	public static final Long nextLabelGroup() {
		Object nextVal = Base.firstCell("SELECT nextval('label_groups')");
		if (nextVal == null) return -1L;
		return Long.valueOf(nextVal.toString());
	}

	public Label(String lang) {
		set("lang", lang);
		set("label_group", nextLabelGroup());
	}

	public Label(Long group, String lang) {
		set("lang", lang);
		set("label_group", group);
	}

}
