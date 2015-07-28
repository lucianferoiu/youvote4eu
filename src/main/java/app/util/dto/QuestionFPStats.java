package app.util.dto;

public class QuestionFPStats {
	public QuestionFPStats(Long id, Long votes, Double voteTally) {
		super();
		this.id = id;
		this.votes = votes;
		this.voteTally = voteTally;
	}

	public Long id;
	public Long votes;
	public Double voteTally;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVotes() {
		return votes;
	}

	public void setVotes(Long votes) {
		this.votes = votes;
	}

	public Double getVoteTally() {
		return voteTally;
	}

	public void setVoteTally(Double voteTally) {
		this.voteTally = voteTally;
	}

}
