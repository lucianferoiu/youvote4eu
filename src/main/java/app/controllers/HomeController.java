package app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Base;

import app.base.AnonAuthController;
import app.base.Const;
import app.models.Citizen;
import app.models.Lang;
import app.models.Question;

/**
 * Responds to homepage request and to small ancillary requests for fragments
 * such as citizen preferences, etc.
 */
public class HomeController extends AnonAuthController {

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

		view("mostSupportedQuestions", mostSupportedQuestions);
		view("newestQuestions", newestQuestions);

	}

	public void help() {}

	public void about() {}

	public void catchall() {
		redirect("/home");
	}

}
