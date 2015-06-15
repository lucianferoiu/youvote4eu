package app.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Many;

/**
 * The question subject to voting by citizens. Class acts also as a façade for
 * some statistical queries regarding the status of the question.
 */
@Many2Many(other = Citizen.class, join = "votes", sourceFKName = "question_id", targetFKName = "citizen_id")
public class Question extends Model {

	static {
		//		Model.validatePresenceOf("title", "description", "proposed_by");
	}

}
