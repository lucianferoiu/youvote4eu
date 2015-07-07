package app.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

		String lang = preferredLang();
		Long tagId = null;
		String tagParam = param("tag");
		if (!StringUtils.nullOrEmpty(tagParam)) {
			tagId = Long.decode(tagParam);
		}
		String searchParam = param("searchKeyword");
		String word = StringUtils.nullOrEmpty(searchParam) || (!searchParam.matches("\\w+")) ? null : searchParam;
		view("searchKeyword", word);
		String filter = param("filter");

		ArrayList alreadyVotedQuestions = (ArrayList) session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN);
		if (alreadyVotedQuestions == null) {
			alreadyVotedQuestions = (ArrayList) Base.firstColumn("select question_id from votes where citizen_id=?", citizenId);
			session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN, alreadyVotedQuestions);
		}

		prepareHeaderValues();

		List<FrontpageQuestion> last3ArchivedQuestions = findQuestions(lang, true, true, null, null, 0L, 3L);
		view("last3ArchivedQuestions", last3ArchivedQuestions);

		//TODO: add the questions to the session and manage them with eventual re-ordering; maybe a caching mechanism..
		//TODO: extract the questions as FrontpageQuestion DTOs
		//TODO: consider the citizen language for translated title and description
		List<FrontpageQuestion> questions = new ArrayList<FrontpageQuestion>();
		if ("archived".equalsIgnoreCase(filter)) {
			questions = findQuestions(lang, true, true, word, tagId);
		} else if ("newest".equalsIgnoreCase(filter)) {
			questions = findQuestions(lang, false, true, word, tagId, 0L, 33L);
		} else {
			List<FrontpageQuestion> newerQuestions = findQuestions(lang, false, true, word, tagId);
			List<FrontpageQuestion> popularQuestions = findQuestions(lang, false, false, word, tagId);
			int howManyNew = Math.floorDiv(newerQuestions.size(), 20);//~%5% newer questions

			//a bit inefficient, but it's more tedious to walk both lists at the same time in a freemarker template...
			final Set<Long> renderedQuestions = new HashSet<Long>();
			int howManyNewerQuestions = newerQuestions.size();
			int howManyPopularQuestions = popularQuestions.size();//these two should be equal, but...
			for (int i = 0; i < Math.max(howManyNewerQuestions, howManyPopularQuestions); i++) {
				if (i < howManyPopularQuestions) {
					FrontpageQuestion fpq = popularQuestions.get(i);
					if (fpq != null && !renderedQuestions.contains(fpq.id)) {
						questions.add(fpq);
						renderedQuestions.add(fpq.id);
					}
				}
				if (i < howManyNewerQuestions && i <= howManyNew) {
					FrontpageQuestion fpq = newerQuestions.get(i);
					if (fpq != null && !renderedQuestions.contains(fpq.id)) {
						questions.add(fpq);
						renderedQuestions.add(fpq.id);
					}
				}
			}
		}
		view("questions", questions);

	}

	public void question() {
		Citizen citizen = (Citizen) session(Const.AUTH_CITIZEN);
		Long citizenId = citizen == null ? -1L : citizen.getLongId();
		String lang = preferredLang();

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
		String lang = preferredLang();
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
