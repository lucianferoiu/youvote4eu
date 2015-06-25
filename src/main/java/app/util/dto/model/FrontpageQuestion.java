package app.util.dto.model;

import java.util.Date;

import app.util.dto.Square;

public class FrontpageQuestion {

	public Long id;
	public String title;
	public String description;
	public Long votesCount;
	public Date publishedOn;
	public Square layout;

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

	public Square getLayout() {
		return layout;
	}

	public void setLayout(Square layout) {
		this.layout = layout;
	}

}
