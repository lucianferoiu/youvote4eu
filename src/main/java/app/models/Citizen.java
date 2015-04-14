package app.models;

import java.util.Date;
import java.util.List;

import org.javalite.activejdbc.Model;

/**
 * The main actor of the voting mechanism. It represents an anonymously
 * authenticated citizen. This class acts also as a façade for the voting
 * process, aggregating some functional methods.
 */
public class Citizen extends Model {
	static {}

	/**
	 * @return the list of votes by this citizen
	 */
	public List<Vote> getVotes() {
		return Vote.find("citizen_id=?", getId());
	}

	/**
	 * @return list of questions upon which the citizen already voted
	 */
	public List<Question> getVotedQuestions() {
		return Question.findBySQL(" select q.* from questions q where "
				+ " q.id in (select v.id from votes v where v.citizen_id=?) ", getId());
	}

	/**
	 * @return list of available questions on which the citizen can vote
	 */
	public List<Question> getQuestionsToVote() {
		return Question.findBySQL(" select q.* from questions q where "
				+ " ( q.closed_at is null or q.closed_at > current_timestamp ) "
				+ " and q.id not in (select v.id from votes v where v.citizen_id=?) ", getId());
	}

	/**
	 * Vote on question
	 * 
	 * @return true if a new vote was successfully cast, false if the citizen
	 *         already voted (or a problem occurred)
	 */
	public boolean vote(Question q, Integer value) {
		if (q.getId() == null) return false;//invalid/unsaved question
		return vote(q.getLongId(), value);
	}

	/**
	 * @return the vote on the question (null if none exists)
	 */
	public Vote findVote(Long question_id) {
		return Vote.findFirst("question_id=? and citizen_id=?", question_id, getId());
	}

	/**
	 * Vote on a question of that id
	 * 
	 * @return true if a new vote was successfully cast, false if the citizen
	 *         already voted or a problem occurred
	 */
	public boolean vote(Long question_id, Integer value) {
		Vote alreadyVoted = findVote(question_id);
		if (alreadyVoted == null) {
			//we should also check if the question actually exists...
			// if (Question.exists(question_id)) {  <--- actually we need the question to test its validity...
			Question question = Question.findById(question_id);
			if (question != null) {
				Date today = new Date();
				Date questionClosingDate = question.getDate("closed_at");
				if (questionClosingDate != null && today.after(questionClosingDate)) return false;//voting on this question is closed
				Vote v = Vote.create("question_id", question_id, "citizen_id", getLongId(), "value", value,
						"validated", get("validated"));
				v.setDate("cast_at", today);//explicit data type needed..
				return v.save();
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
