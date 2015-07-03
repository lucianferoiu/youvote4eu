package app.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javalite.activejdbc.Base;

import app.base.Const;
import app.base.QuestionsListController;
import app.models.Citizen;
import app.models.Lang;
import app.models.Question;
import app.services.QuestionsLayouter;
import app.util.JsonHelper;
import app.util.dto.QuestionCell;
import app.util.dto.Square;
import app.util.dto.model.FrontpageQuestion;

import com.google.inject.Inject;

/**
 * Responds to homepage request and to small ancillary requests for fragments
 * such as citizen preferences, etc.
 */
public class HomeController extends QuestionsListController {

	@Inject
	protected QuestionsLayouter layouter;

	public void index() {
		List<Lang> langs = Lang.findAll().orderBy("code");//TODO add the languages to the app context - they are immutable and we can save a DB roundtrip
		view("langs", langs);
		//		setEncoding("UTF-8");

		Citizen citizen = (Citizen) session(Const.AUTH_CITIZEN);
		Long citizenId = citizen == null ? -1L : citizen.getLongId();

		String lang = preferredLang();

		ArrayList alreadyVotedQuestions = (ArrayList) session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN);
		if (alreadyVotedQuestions == null) {
			alreadyVotedQuestions = (ArrayList) Base.firstColumn("select question_id from votes where citizen_id=?", citizenId);
			session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN, alreadyVotedQuestions);
		}

		//TODO: add the questions to the session and manage them with eventual re-ordering; maybe a caching mechanism..
		//TODO: extract the questions as FrontpageQuestion DTOs
		//TODO: consider the citizen language for translated title and description

		List<FrontpageQuestion> newerQuestions = findQuestions(lang, false, true, null, null);
		List<FrontpageQuestion> popularQuestions = findQuestions(lang, false, false, null, null);

		//a bit inefficient, but it's more tedious to walk both lists at the same time in a freemarker template...
		List<FrontpageQuestion> questions = new ArrayList<FrontpageQuestion>();
		final Set<Long> renderedQuestions = new HashSet<Long>();
		int howManyNewerQuestions = newerQuestions.size();
		int howManyPopularQuestions = popularQuestions.size();//these two should be equal, but...
		for (int i = 0; i < Math.max(howManyNewerQuestions, howManyPopularQuestions); i++) {
			if (i < howManyPopularQuestions) {
				FrontpageQuestion fpq = popularQuestions.get(i);
				if (fpq != null && !renderedQuestions.contains(fpq.id)) {
					questions.add(fpq);
				}
			}
			if (i < howManyNewerQuestions) {
				FrontpageQuestion fpq = newerQuestions.get(i);
				if (fpq != null && !renderedQuestions.contains(fpq.id)) {
					questions.add(fpq);
				}
			}
		}
		view("questions", questions);

	}

	@Deprecated
	public void indexx() {
		List<Lang> langs = Lang.findAll().orderBy("code");//TODO add the languages to the app context - they are immutable and we can save a DB roundtrip
		view("langs", langs);

		Citizen citizen = (Citizen) session(Const.AUTH_CITIZEN);
		Long citizenId = -1L;//TODO retrieve the real citizen ID

		ArrayList alreadyVotedQuestions = (ArrayList) session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN);
		if (alreadyVotedQuestions == null) {
			alreadyVotedQuestions = (ArrayList) Base.firstColumn("select question_id from votes where citizen_id=?", citizenId);
			session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN, alreadyVotedQuestions);
		}

		//TODO: add the questions to the session and manage them with eventual re-ordering; maybe a caching mechanism..
		//TODO: extract the questions as FrontpageQuestion DTOs
		//TODO: consider the citizen language for translated title and description

		List<Question> mostSupportedQuestions = Question
				.findBySQL("SELECT id,title,description,popular_votes,open_at FROM questions WHERE is_published=true AND is_archived=false AND is_deleted=false ORDER BY popular_votes desc, open_at desc ");
		List<Question> newestQuestions = Question
				.findBySQL("SELECT id,title,description,popular_votes,open_at FROM questions WHERE is_published=true AND is_archived=false AND is_deleted=false ORDER BY open_at desc, popular_votes desc ");

		List<QuestionCell> cells = new ArrayList<QuestionCell>();
		List<Question> questions = new ArrayList<Question>();

		final Set<Long> renderedQuestions = new HashSet<Long>();
		List<Square> layouts = layouter.randomLayout();
		int offset = 0;
		while ((!mostSupportedQuestions.isEmpty()) || (!newestQuestions.isEmpty())) {
			if (layouts == null || layouts.isEmpty()) {
				layouts = layouter.randomLayout();
				offset += 10;
			}
			Square sq = layouts.remove(0);
			if (sq == null) break;
			List<Question> runningList = (sq.sz == 1) ? (newestQuestions.isEmpty() ? mostSupportedQuestions : newestQuestions)
					: (mostSupportedQuestions.isEmpty() ? newestQuestions : mostSupportedQuestions);
			Question q = null;
			do {
				if (runningList.isEmpty()) break;
				q = runningList.remove(0);
			}
			while (renderedQuestions.contains(q.getLongId()));
			if (q == null) continue;
			Long qid = q.getLongId();
			renderedQuestions.add(qid);
			questions.add(q);
			cells.add(new QuestionCell(sq.x, sq.y + offset, sq.sz, qid));

		}

		String cellsJSON = JsonHelper.toListJson(cells);
		view("questions", questions);
		view("cells", cells);
		view("cellsAsJSON", cellsJSON);
		view("maxGridHeight", offset + 10);

	}

	public void help() {}

	public void about() {}

	public void catchall() {
		redirect("/home");
	}

}
