package app.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;

@BelongsTo(parent = Question.class, foreignKeyName = "question_id")
public class Comment extends Model {
	static {
		//		Model.validatePresenceOf("created_by");
	}
}
