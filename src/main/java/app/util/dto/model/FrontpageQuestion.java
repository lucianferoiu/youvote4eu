package app.util.dto.model;

import java.util.Date;

public class FrontpageQuestion {

	public Long id;
	public String title;
	public String description;
	public Long votesCount;
	public Date publishedOn;
	public Date archivedOn;
	public boolean isNew;
	public boolean isArch;
	public Integer rank;
	public Double voteTally;
	public Double officialVoteTally;
	public Boolean canVote;
	public Integer voted;

	//bloody freemarker, makes me generate cluttering getters/setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getVotesCount() {
		return votesCount;
	}

	public void setVotesCount(Long votesCount) {
		this.votesCount = votesCount;
	}

	public Date getPublishedOn() {
		return publishedOn;
	}

	public void setPublishedOn(Date publishedOn) {
		this.publishedOn = publishedOn;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public boolean isArch() {
		return isArch;
	}

	public void setArch(boolean isArch) {
		this.isArch = isArch;
	}

	public Date getArchivedOn() {
		return archivedOn;
	}

	public void setArchivedOn(Date archivedOn) {
		this.archivedOn = archivedOn;
	}

	public Double getVoteTally() {
		return voteTally;
	}

	public void setVoteTally(Double voteTally) {
		this.voteTally = voteTally;
	}

	public Double getOfficialVoteTally() {
		return officialVoteTally;
	}

	public void setOfficialVoteTally(Double officialVoteTally) {
		this.officialVoteTally = officialVoteTally;
	}

	public boolean getCanVote() {
		return canVote;
	}

	public void setCanVote(boolean canVote) {
		this.canVote = canVote;
	}

	public Integer getVoted() {
		return voted;
	}

	public void setVoted(Integer voted) {
		this.voted = voted;
	}

}
