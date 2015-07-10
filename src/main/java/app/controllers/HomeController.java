package app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.Const;
import app.base.QuestionsListController;
import app.models.Citizen;
import app.models.Lang;
import app.models.Question;
import app.models.Tag;
import app.models.Translation;
import app.services.QuestionsLayouter;
import app.util.StringUtils;
import app.util.dto.model.CountedTag;
import app.util.dto.model.FrontpageQuestion;

import com.google.inject.Inject;

/**
 * Responds to homepage request and to small ancillary requests for fragments
 * such as citizen preferences, etc.
 */
public class HomeController extends QuestionsListController {

	static final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Inject
	protected QuestionsLayouter layouter;

	public void index() {
		Citizen citizen = (Citizen) session(Const.AUTH_CITIZEN);
		Long citizenId = citizen == null ? -1L : citizen.getLongId();

		String lang = preferredLangCode();
		Long tagId = null;
		String tagParam = param("tagId");
		if (!StringUtils.nullOrEmpty(tagParam)) {
			tagId = Long.decode(tagParam);
		}
		String searchParam = param("searchKeyword");
		String word = StringUtils.nullOrEmpty(searchParam) || (!searchParam.matches("\\w+")) ? null : searchParam;
		view("searchKeyword", word);

		String questionsKind = param("questionsKind");

		ArrayList alreadyVotedQuestions = (ArrayList) session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN);
		if (alreadyVotedQuestions == null) {
			alreadyVotedQuestions = (ArrayList) Base.firstColumn("select question_id from votes where citizen_id=?", citizenId);
			session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN, alreadyVotedQuestions);
		}

		prepareHeaderValues();

		List<FrontpageQuestion> last3ArchivedQuestions = findQuestions(lang, true, true, null, null, 0L, 3L);
		view("last3ArchivedQuestions", last3ArchivedQuestions);

		List<FrontpageQuestion> questions = null;//new ArrayList<FrontpageQuestion>();
		if ("archived".equalsIgnoreCase(questionsKind)) {
			questions = findQuestions(lang, true, true, word, tagId);
			view("activeFilter", "archived");
		} else if ("newest".equalsIgnoreCase(questionsKind)) {
			questions = findQuestions(lang, false, true, word, tagId, 0L, 33L);
			view("activeFilter", "newest");
		} else {
			questions = findQuestions(lang, false, false, word, tagId);
			if (tagId != null) {
				view("activeFilter", "tag");
				view("filterTagId", tagId);
			} else {
				view("activeFilter", "popular");
			}
		}
		view("questions", questions);

	}

	public void question() {
		Citizen citizen = (Citizen) session(Const.AUTH_CITIZEN);
		Long citizenId = citizen == null ? -1L : citizen.getLongId();
		String lang = preferredLangCode();

		Long qId = null;
		String qIdParam = param("id");
		if (StringUtils.nullOrEmpty(qIdParam)) {
			log.warn("Missing question ID param - citizen id: " + citizenId);
			redirect("/home");
			return;
		}
		qId = Long.decode(qIdParam);
		if (qId == null) {
			log.warn("Wrong question ID param: " + qIdParam + " - citizen id: " + citizenId);
			redirect("/home");
			return;

		}

		Question question = Question.findById(qId);
		if (question == null) {
			log.warn("Cannot find question with ID: " + qIdParam + " - citizen id: " + citizenId);
			redirect("/home");
			return;
		}
		view("question", question);

		List<Tag> questionTags = question.getAll(Tag.class);
		view("questionTags", questionTags);

		prepareQuestionTranslations(question, lang);
		prepareHeaderValues();
	}

	public void archived() {
		question();
		String lang = preferredLangCode();
		List<FrontpageQuestion> moreArchivedQuestions = findQuestions(lang, true, false, null, null, 0L, 10L);
		view("moreArchivedQuestions", moreArchivedQuestions);
	}

	public void help() {}

	public void about() {}

	public void catchall() {
		redirect("/home");
	}

	//////////////////////////////////

	protected void prepareHeaderValues() {
		List<Lang> langs = Lang.findAll().orderBy("code");//TODO add the languages to the app context - they are immutable and we can save a DB roundtrip
		view("langs", langs);
		List<CountedTag> tags = tagsByPubQuestionsCount();
		view("tags", tags);
		Lang sessionLang = (Lang) session(Const.CURRENT_LANGUAGE);
		view("preferredLang", sessionLang);
	}

	protected void prepareQuestionTranslations(Question question, String lang) {
		String title = question.getString("title");
		String description = question.getString("description");
		String html_content = question.getString("html_content");
		if (!"en".equalsIgnoreCase(lang)) {
			List<Translation> translations = question.get(Translation.class, "lang=?", lang);
			for (Translation tr : translations) {
				String field_type = tr.getString("field_type");
				String text = tr.getString("text");
				if ("title".equalsIgnoreCase(field_type)) {
					title = text;
				} else if ("description".equalsIgnoreCase(field_type)) {
					description = text;
				} else if ("html_content".equalsIgnoreCase(field_type)) {
					html_content = text;
				} else {
					log.warn("Superfluous translation of field {} (id={}) for question {}", field_type, tr.getLongId(),
							question.getLongId());
				}
			}
		}
		view("questionTitle", title);
		view("questionDescription", description);
		view("questionHtmlContent", html_content);
	}

}
