package app.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;

@BelongsTo(parent = Question.class, foreignKeyName = "question_id")
public class Upvote extends Model {

}
