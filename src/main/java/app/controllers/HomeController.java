package app.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javalite.activejdbc.Base;

import app.base.AnonAuthController;
import app.base.Const;
import app.models.Citizen;
import app.models.Lang;
import app.models.Question;
import app.services.QuestionsLayouter;
import app.util.dto.Square;
import app.util.dto.model.FrontpageQuestion;

import com.google.inject.Inject;

/**
 * Responds to homepage request and to small ancillary requests for fragments
 * such as citizen preferences, etc.
 */
public class HomeController extends AnonAuthController {

	@Inject
	protected QuestionsLayouter layouter;

	public void index() {
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

		List<Square> layouts = layouter.randomLayout();
		List<FrontpageQuestion> questions = new ArrayList<FrontpageQuestion>();
		final Set<Long> renderedQuestions = new HashSet<Long>();
		/*
		String q = " SELECT 0 as kind,id,title,description,popular_votes,open_at FROM questions WHERE is_published=true AND is_archived=false AND is_deleted=false ORDER BY popular_votes desc, open_at desc "
				+ " UNION "
				+ " SELECT 1 as kind,id,title,description,popular_votes,open_at FROM questions WHERE is_published=true AND is_archived=false AND is_deleted=false ORDER BY open_at desc, popular_votes desc ";
		Base.find(q).with(new RowListenerAdapter() {
			@Override
			public void onNext(Map<String, Object> row) {
				Long id = (Long) row.get("id");
				if (id != null && !renderedQuestions.contains(id)) {
					FrontpageQuestion fq = new FrontpageQuestion();
					fq.id = id;
					fq.title = (String) row.get("title");
					fq.description = (String) row.get("description");
					fq.votesCount = (Long) row.get("popular_votes");
					Timestamp pubOn = (Timestamp) row.get("open_at");
					if (pubOn != null) fq.publishedOn = new Date(pubOn.toInstant().getEpochSecond());
					questions.add(fq);
					renderedQuestions.add(id);
				}
			}
		});
		*/

		while ((!mostSupportedQuestions.isEmpty()) || (!newestQuestions.isEmpty())) {
			if (layouts == null || layouts.isEmpty()) {
				layouts = layouter.randomLayout();
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
			FrontpageQuestion fq = new FrontpageQuestion();
			fq.id = qid;
			fq.title = (String) q.get("title");
			fq.description = (String) q.get("description");
			fq.votesCount = (Long) q.get("popular_votes");
			Timestamp pubOn = q.getTimestamp("open_at");
			if (pubOn != null) fq.publishedOn = new Date(pubOn.toInstant().getEpochSecond());
			fq.layout = sq;
			questions.add(fq);

		}
		view("questions", questions);

	}

	public void help() {}

	public void about() {}

	public void catchall() {
		redirect("/home");
	}

}
