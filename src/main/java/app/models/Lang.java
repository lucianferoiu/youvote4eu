package app.models;

import org.javalite.activejdbc.Model;

public class Lang extends Model {
	static {
		//		Model.validatePresenceOf("code");
	}

	public String getNative() {
		String ln = getString("label_native");
		return ln;
	}
}
