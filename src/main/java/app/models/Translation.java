package app.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

@BelongsToPolymorphic(//
parents = { Question.class, Tag.class, Country.class }, //
typeLabels = { "question", "tag", "country" } //
)
public class Translation extends Model {
	static {
		Model.validatePresenceOf("lang");
	}
}
