package app.models;

import java.util.List;

import org.javalite.activejdbc.Model;

/**
 * The main actor of the voting mechanism. It represents an anonymously
 * authenticated citizen.
 */
public class Citizen extends Model {
	static {}

	public List<Vote> getVotes() {
		return Vote.find("citizen_id=?", getId());
	}

	public List<Question> getVotedQuestions() {
		return Question.findBySQL(" select q.* from questions q where "
				+ " q.id in (select v.id from votes v where v.citizen_id=?) ", getId());
	}

	public List<Question> getQuestionsToVote() {
		return Question.findBySQL(" select q.* from questions q where "
				+ " ( q.closed_at is null or q.closed_at > current_timestamp ) "
				+ " and q.id not in (select v.id from votes v where v.citizen_id=?) ", getId());
	}
}
