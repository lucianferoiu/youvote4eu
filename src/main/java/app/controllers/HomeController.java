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

import com.google.inject.Inject;

/**
 * Responds to homepage request and to small ancillary requests for fragments
 * such as citizen preferences, etc.
 */
public class HomeController extends QuestionsListController {

	@Inject
	protected QuestionsLayouter layouter;

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
