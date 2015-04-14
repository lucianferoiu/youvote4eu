package app.models;

import java.util.List;

import org.javalite.activejdbc.Model;

/**
 * The question subject to voting by citizens. Class acts also as a façade for
 * some statistical queries regarding the status of the question.
 */
public class Question extends Model {

	public String getDescription(String lang) {
		Label l = Label.findFirst("label_group=? and lang=?", get("description_id"), lang);
		if (l == null) return getString("description_en");//defaults to english if no label was found..
		return l.getString("text");
	}

	/**
	 * Sets the English description of the question (no save)
	 */
	public void setDescription(String text) {
		set("description_en", text);
	}

	/**
	 * Saves the description for the provided language by locating/creating the
	 * appropriate label. May throw exception upon failure to save.
	 */
	public void saveDescription(String lang, String text) {
		saveLabel("description", lang, text);
	}

	/**
	 * Sets the English title of the question (no save)
	 */
	public void setTitle(String text) {
		set("title_en", text);
	}

	/**
	 * Saves the title for the provided language by locating/creating the
	 * appropriate label. May throw exception upon failure to save.
	 */
	public void saveTitle(String lang, String text) {
		saveLabel("title", lang, text);
	}

	/**
	 * Sets the English HTML content of the question (no save)
	 */
	public void setHtmlContent(String text) {
		set("html_content_en", text);
	}

	/**
	 * Saves the HTML content for the provided language by locating/creating the
	 * appropriate label. May throw exception upon failure to save.
	 */
	public void saveHtmlContent(String lang, String text) {
		saveLabel("html_content", lang, text);
	}

	private void saveLabel(String field, String lang, String text) {
		Object group = get(field + "_id");
		if (group != null) {//this label has been set before, either for this language or for another...
			Long labelGroup = Long.valueOf(group.toString());
			Label l = Label.findFirst("label_group=? and lang=?", labelGroup, lang);
			if (l != null) {//this is an update
				l.set("text", text);
				l.saveIt();
			} else {//this is an insert
				l = new Label(labelGroup, lang);
				l.set("text", text);
				l.saveIt();
				set(field + "_id", l.get("label_group"));
				saveIt();
			}
		} else {//brand new label
			Label l = new Label(lang);
			l.set("text", text);
			l.saveIt();
			set(field + "_id", l.get("label_group"));
			saveIt();

		}
	}

	//

	public List<Vote> getVotes() {
		return getAll(Vote.class);
	}

	public Long countAllVotes() {
		return Vote.count("question_id=?", getId());
	}

	public Long countValidVotes() {
		return Vote.count("question_id=? and validated=?", getId(), true);
	}

	public Long countValidYesVotes() {
		return Vote.count("question_id=? and validated=? and value=?", getId(), true, 1);
	}

	public Long countValidNoVotes() {
		return Vote.count("question_id=? and validated=? and value=?", getId(), true, 0);
	}

	public Double agreementPercentage() {
		// current implementation requires a double trip to the DB... TODO improve along the lines:
		//	 Base.exec(
		//		" SELECT count(a)/count(b) FROM votes a, votes b WHERE "
		//			+ " a.question_id=? and b.question_id=? "
		//			+ " and a.validated and b.validated and a.value=1 and b.value is not null ",
		//		getId(), getId());

		Double validVotes = countValidVotes().doubleValue();
		if (validVotes == null || validVotes <= 0) return 0D;
		Double yesVotes = countValidYesVotes().doubleValue();
		if (yesVotes == null || yesVotes <= 0) return 0D;
		return yesVotes / validVotes;
	}
}
