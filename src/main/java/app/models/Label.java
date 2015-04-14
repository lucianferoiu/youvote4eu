package app.models;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

import app.util.StringUtils;

public class Label extends Model {
	static {
		Model.validatePresenceOf("lang");
	}

	/**
	 * Dumb constructor creating a label for English within an unsupported
	 * labels group. This is just a convenience method to allow creation of
	 * instances through introspection; should not be called explicitly in code
	 */
	public Label() {
		set("lang", "en");
		set("label_group", -1L);
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

	public Long getGroup() {
		return getLong("label_group");
	}

	//----------------

	public static Label saveLabel(String lang, Long group, String text) {
		if (StringUtils.nullOrEmpty(lang)) return null;
		Label l = null;
		if (group == null) {
			l = new Label(lang);
		} else {
			l = Label.findFirst("label_group=? and lang=?", group, lang);
			if (l != null) {//this is an update
			} else {//this is an insert
				l = new Label(group, lang);
				l.set("lang", lang);
			}
		}
		l.set("text", StringUtils.nullOrEmpty(lang) ? "" : text);
		l.saveIt();
		return l;
	}

}
