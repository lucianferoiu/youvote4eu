package app.util.dto;

import java.util.Date;

public class VotingEngagementDTO {

	public String countryCode;
	public String countryName;
	public Date votedOn;
	public Integer voteValue;
	public Boolean validated;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Date getVotedOn() {
		return votedOn;
	}

	public void setVotedOn(Date votedOn) {
		this.votedOn = votedOn;
	}

	public Integer getVoteValue() {
		return voteValue;
	}

	public void setVoteValue(Integer voteValue) {
		this.voteValue = voteValue;
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}

}
