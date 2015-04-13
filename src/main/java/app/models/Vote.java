package app.models;

import org.javalite.activejdbc.Model;

public class Vote extends Model {
	static {
		Model.validatePresenceOf("question_id", "citizen_id", "value");
	}

}
