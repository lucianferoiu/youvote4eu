package app.models;

import org.javalite.activejdbc.Model;

/**
 * The vote cast by a citizen on a question. It's a many-to-many
 * relationship/association between a question and a citizen (not an entity in
 * itself).
 */
public class Vote extends Model {
	static {
		//		Model.validatePresenceOf("value");
	}

}
