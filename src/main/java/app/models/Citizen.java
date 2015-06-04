package app.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Many;

/**
 * The main actor of the voting mechanism. It represents an anonymously
 * authenticated citizen. This class acts also as a façade for the voting
 * process, aggregating some functional methods.
 */
@Many2Many(other = Question.class, join = "votes", sourceFKName = "citizen_id", targetFKName = "question_id")
public class Citizen extends Model {
	static {}

}
