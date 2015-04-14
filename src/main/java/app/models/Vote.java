package app.models;

import org.javalite.activejdbc.Model;

/**
 * The vote cast by a citizen on a question. It's more like a
 * relationship/association, not like an entity in itself.
 */
public class Vote extends Model {
	static {
		Model.validatePresenceOf("question_id", "citizen_id", "value");
	}

}
