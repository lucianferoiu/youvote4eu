package app.controllers;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.RowListenerAdapter;
import org.javalite.activeweb.Configuration;
import org.javalite.activeweb.Cookie;
import org.javalite.activeweb.annotations.GET;
import org.javalite.activeweb.annotations.POST;
import org.javalite.activeweb.annotations.PUT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.Const;
import app.base.QuestionsListController;
import app.models.Citizen;
import app.models.Country;
import app.models.EmailValidation;
import app.models.Lang;
import app.models.Question;
import app.models.Tag;
import app.models.Token;
import app.models.Translation;
import app.models.Vote;
import app.services.QuestionsLayouter;
import app.util.JsonHelper;
import app.util.StringUtils;
import app.util.dto.QuestionFPStats;
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
		boolean asJson = "array".equalsIgnoreCase(param("format")) || "objects".equalsIgnoreCase(param("format"));//as/json or .json etc. are killed by the front-server, so they cannot be used as segments

		Citizen citizen = getOrCreateCitizen();
		Long citizenId = citizen == null ? -1L : citizen.getLongId();

		String lang = preferredLangCode();
		Long tagId = null;
		String tagParam = param("tagId");
		if (!StringUtils.nullOrEmpty(tagParam)) {
			tagId = Long.decode(tagParam);
		}
		String questionsKind = param("questionsKind");
		String searchParam = param("searchKeyword");
		String word = StringUtils.nullOrEmpty(searchParam) || (!searchParam.matches("\\w+")) ? null : searchParam;
		if (!asJson) view("searchKeyword", word);

		HashMap<Long, Integer> alreadyVotedQuestions = null;
		synchronized (Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN) {
			alreadyVotedQuestions = (HashMap<Long, Integer>) session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN);
			if (alreadyVotedQuestions == null) {
				final HashMap<Long, Integer> citizenVotes = new HashMap<Long, Integer>();
				Base.find("select question_id, value from votes where citizen_id=?", citizenId).with(new RowListenerAdapter() {

					@Override
					public void onNext(Map<String, Object> row) {
						Long id = (Long) row.get("question_id");
						Integer value = (Integer) row.get("value");
						citizenVotes.put(id, value);
					}

				});
				alreadyVotedQuestions = new HashMap<Long, Integer>(citizenVotes);
				session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN, alreadyVotedQuestions);
			}
		}

		if (!asJson) prepareHeaderValues();

		if (!asJson) {
			List<FrontpageQuestion> last3ArchivedQuestions = findQuestions(lang, true, true, null, null, 0L, 3L);
			view("last3ArchivedQuestions", last3ArchivedQuestions);
		}

		List<FrontpageQuestion> questions = null;//new ArrayList<FrontpageQuestion>();
		if ("archived".equalsIgnoreCase(questionsKind)) {
			questions = findQuestions(lang, true, true, word, tagId);
			if (!asJson) view("activeFilter", "archived");
		} else if ("newest".equalsIgnoreCase(questionsKind)) {
			questions = findQuestions(lang, false, true, word, tagId, 0L, 33L);
			if (!asJson) view("activeFilter", "newest");
		} else {
			questions = findQuestions(lang, false, false, word, tagId);
			if (!asJson) {
				if (tagId != null) {
					view("activeFilter", "tag");
					view("filterTagId", tagId);
				} else {
					view("activeFilter", "popular");
				}
			}
		}

		for (FrontpageQuestion fpq : questions) {
			Integer voteValue = alreadyVotedQuestions.get(fpq.id);
			if (voteValue != null) {
				fpq.canVote = false;
				fpq.voted = voteValue;
			} else {
				fpq.canVote = !(fpq.isArch);
			}

		}
		boolean validatedCitizen = citizen != null && citizen.getBoolean("validated");
		view("validatedCitizen", validatedCitizen);
		if (!validatedCitizen) {
			Long pendingValidations = EmailValidation.count("added_by=? AND validated=false AND is_citizen=true", citizenId);
			view("pendingValidation", pendingValidations > 0);
		} else {
			view("pendingValidation", false);
		}

		if (asJson) {
			String json = JsonHelper.toListJson(questions);
			respond(json).contentType("application/json").status(200);
			return;
		} else {
			view("questions", questions);
		}
	}

	public void question() {
		Citizen citizen = getOrCreateCitizen();
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

		Vote myVote = Vote.findFirst("question_id=? and citizen_id=?", qId, citizenId);
		view("canVote", new Boolean(myVote == null));
		view("voted", myVote != null ? myVote.getInteger("value") : -1);

		List<Tag> questionTags = question.getAll(Tag.class);
		view("questionTags", questionTags);

		prepareQuestionTranslations(question, lang);
		prepareHeaderValues();
		view("activeFilter", "none");
		view("validatedCitizen", citizen != null && citizen.getBoolean("validated"));
		Long pendingValidations = EmailValidation.count("added_by=? AND validated=false AND is_citizen=true", citizenId);
		view("pendingValidation", pendingValidations > 0);
	}

	public void archived() {
		question();
		String lang = preferredLangCode();
		List<FrontpageQuestion> moreArchivedQuestions = findQuestions(lang, true, false, null, null, 0L, 10L);
		view("moreArchivedQuestions", moreArchivedQuestions);
	}

	public void help() {}

	public void privacy() {}

	public void code() {}

	public void about() {}

	public void contact() {}

	public void catchall() {
		redirect("/home");
	}

	@POST
	public void castVote() {
		String email = param("citizen-email");
		String countryCode = param("citizen-country");
		String qId = param("qId");
		String voteValueParam = param("voteValue");
		if (!StringUtils.nullOrEmpty(email) && !StringUtils.nullOrEmpty(countryCode)) {
			sendValidationEmail();
		}
		if (!StringUtils.nullOrEmpty(qId) && !StringUtils.nullOrEmpty(voteValueParam)) {
			vote();
		}
		if (!StringUtils.nullOrEmpty(qId)) {
			redirect("/question/" + qId);
		} else {
			redirect("/home");
		}
	}

	@PUT
	public void vote() {
		String qIdParam = param("qId");
		String voteValueParam = param("voteValue");
		Long qId = null;
		Integer voteValue = null;
		if (!StringUtils.nullOrEmpty(qIdParam)) {
			qId = Long.decode(qIdParam);
		}
		if (!StringUtils.nullOrEmpty(voteValueParam)) {
			voteValue = Integer.decode(voteValueParam);
		}

		if (qId == null || voteValue == null) {
			respond("wrong parameters").contentType("application/json").status(400);
			return;
		}

		Base.openTransaction();
		Question q = Question.findById(qId);
		if (q == null) {
			respond("question not found").contentType("application/json").status(404);
			Base.rollbackTransaction();
			return;
		}

		Citizen citizen = getOrCreateCitizen();
		if (citizen != null) {
			Long duplicates = Vote.count("question_id=? AND citizen_id=?", qId, citizen.getLongId());
			if (duplicates == null || duplicates <= 0) {//extra-cautious to avoid duplicates (i.e. when using two different devices simultaneously, the session questions can be obsolete)
				Vote vote = Vote.create("question_id", qId, "citizen_id", citizen.getLongId(), //
						"value", voteValue, "validated", citizen.getBoolean("validated"));
				vote.setTimestamp("cast_at", new Date()).saveIt();
				synchronized (Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN) {
					HashMap<Long, Integer> alreadyVoted = (HashMap<Long, Integer>) session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN);
					if (alreadyVoted == null) {
						alreadyVoted = new HashMap<Long, Integer>();
					}
					alreadyVoted.put(qId, voteValue);
					session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN, alreadyVoted);
				}
			} else {
				log.warn("Duplicate vote attempted for question {} by citizen {}", qId, citizen.getLongId());
			}

			Long currentVoteCount = q.getLong("popular_votes");
			BigDecimal currentVoteTally = q.getBigDecimal("popular_vote_tally");
			//TODO: relegate theese as batch operations...
			Long yesVotes = Vote.count("question_id=? AND value=1 AND validated=true", q.getLongId());
			Long noVotes = Vote.count("question_id=? AND value=0 AND validated=true", q.getLongId());
			//
			Long popularVotes = 0L;
			BigDecimal popularVoteTally = BigDecimal.ZERO;
			if (yesVotes > 0 || noVotes > 0) {
				popularVotes = yesVotes + noVotes;
				popularVoteTally = new BigDecimal(yesVotes).divide(new BigDecimal(yesVotes + noVotes), 3, RoundingMode.HALF_UP);
			} else {
				popularVotes = currentVoteCount + 1 - duplicates;
			}
			q.setLong("popular_votes", popularVotes);
			q.setBigDecimal("popular_vote_tally", popularVoteTally);
			q.saveIt();

			QuestionFPStats qStats = new QuestionFPStats(qId, popularVotes, popularVoteTally.doubleValue());

			Base.commitTransaction();
			respond(JsonHelper.toJson(qStats)).contentType("application/json").status(200);

		} else {
			log.debug("No citizen associated with the request from {}::{} - first-time access?", header("Remote_Addr"),
					header("HTTP_X_FORWARDED_FOR"));
			respond("cannot identify citizen").contentType("application/json").status(400);
			return;
		}
	}

	protected Citizen getOrCreateCitizen() {
		Citizen citizen = (Citizen) session(Const.AUTH_CITIZEN);
		if (citizen == null) {//first-time voter
			Cookie c = cookie(Const.AUTH_COOKIE_NAME);
			if (c != null) {
				String token = c.getValue();
				if (StringUtils.nullOrEmpty(token)) {
					respond("no authentication token").contentType("application/json").status(403);
					return null;
				}
				Token authToken = Token.findFirst("token=?", token);
				if (authToken == null) {//no token in the db - we're going to associate it with a new citizen
					String lang = preferredLangCode();
					citizen = Citizen.createIt("validated", false, "lang", lang);
					authToken = Token.createIt("token", token, "validated", false, "citizen_id", citizen.getLongId());
					session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN, null);
				} else {
					citizen = Citizen.findById(authToken.getLong("citizen_id"));
				}
				if (citizen == null) {
					respond("wrong citizen or token information").contentType("application/json").status(400);
					return null;
				}
				session(Const.AUTH_CITIZEN, citizen);
			} else {
				log.warn("Empty auth cookie - first-time access or cookies are disabled?");
				return null;
			}
		}
		return citizen;
	}

	@POST
	public void sendValidationEmail() {
		String email = param("citizen-email");
		String countryCode = param("citizen-country");
		if (StringUtils.nullOrEmpty(email) || (!Const.VALID_EMAIL_ADDRESS_REGEX.matcher(email).find())) {
			flash("citizen_identification_failure", "wrong_params");
			redirect("/error");
			return;
		}
		email = email.trim().toLowerCase();//sanitize the email address
		String lang = preferredLangCode();

		Citizen citizen = getOrCreateCitizen();
		if (citizen == null) {
			redirect("/home");
			return;
		}

		try {
			String url = StringUtils.nvl(url());
			String uri = StringUtils.nvl(uri());
			int pos = url.indexOf(uri, protocol().length() + 1);
			String reqHostname = StringUtils.nvl(pos > 0 ? url.substring(0, pos) : url);
			String shaEmail = messageDigester.digest(email, true);
			String valUrl = reqHostname + "/validate-citizen?code=" + URLEncoder.encode(shaEmail, "UTF-8");
			String subj = "Validate your email so you may vote on YouVoteForEurope";
			Map<String, Object> vals = new HashMap<String, Object>();
			vals.put("url", valUrl);

			StringWriter writer = new StringWriter();
			Configuration.getTemplateManager().merge(vals, "/other/mail/citizenValidation", null, null, writer);

			LocalDateTime now = LocalDateTime.now();
			Date tomorrow = Date.from(now.plusHours(24).toInstant(ZoneOffset.UTC));
			EmailValidation validation = EmailValidation.create("email", shaEmail, "token", shaEmail, "validated", false, "added_by",
					citizen.getLongId());
			validation.setTimestamp("valid_until", tomorrow);
			validation.setBoolean("is_citizen", true);//the kind of email verification
			validation.saveIt();

			if (!StringUtils.nullOrEmpty(countryCode)) {
				citizen.set("country", countryCode).saveIt();
			}

			mailer.sendMail(email, subj, writer.toString(), true);
			log.debug("Citizen identification: created email validation {} and sent the validation email for URL {}", validation, url);
		}
		catch (UnsupportedEncodingException uee) {
			log.warn("Cannot encode SHA(email) as UTF-8", uee);
		}
		redirect("/home");
	}

	@GET
	public void validateCitizen() {
		String code = param("code");
		if (StringUtils.nullOrEmpty(code)) {
			flash("citizen_identification_failure", "validation_code_error");
			log.warn("Error in citizen validation: missing code parameter");
			redirect("/home");
			return;
		}

		try {
			code = URLDecoder.decode(code, "UTF-8");
		}
		catch (UnsupportedEncodingException uee) {
			log.warn("Cannot decode SHA(email) from UTF-8", uee);
		}

		EmailValidation validation = EmailValidation.findCitizenValidation(code);
		if (validation == null) {//no validation or expired
			log.warn("Email validation error: cannot find one for code {}", code);
			flash("citizen_identification_failure", "validation_timeout");
			redirect("/home");
			return;
		}

		//all's well
		Citizen citizen = null;
		session(Const.AUTH_CITIZEN, null);
		String token = cookieValue(Const.AUTH_COOKIE_NAME);
		if (StringUtils.nullOrEmpty(token)) {
			respond("no authentication token").status(403);
			return;
		}

		Base.openTransaction();

		Token authToken = Token.findFirst("token=?", token);
		if (authToken == null) {//no token in the db - we're going to associate it with a new citizen
			citizen = Citizen.findById(validation.getLong("added_by"));
		} else {
			citizen = Citizen.findById(authToken.getLong("citizen_id"));
		}
		if (citizen == null) {
			citizen = Citizen.createIt("");
		}
		session(Const.QUESTIONS_ALREADY_VOTED_BY_CITIZEN, null);

		Long cId = citizen.getLongId();
		List<EmailValidation> pastValidationsOfSameToken = EmailValidation.find("token=? and validated=true and is_citizen=true", code)
				.orderBy("updated_at ASC");
		if (!pastValidationsOfSameToken.isEmpty()) {//citizen re-logging from other machine... transfer all new votes to the old one
			EmailValidation oldestValidation = pastValidationsOfSameToken.get(0);
			Long pastCitizenId = oldestValidation.getLong("added_by");
			Base.exec("UPDATE votes v SET citizen_id=?, validated=true WHERE v.citizen_id=? AND NOT EXISTS "
					+ " (SELECT id FROM votes WHERE citizen_id=? AND question_id=v.question_id AND validated=true) ", //
					pastCitizenId, cId, pastCitizenId);//transfer votes - but leave the duplicates alone...
			//TODO: recompute all questions' votes and tallies
			citizen = Citizen.findById(pastCitizenId);
			session(Const.AUTH_CITIZEN, citizen);
			citizen.setBoolean("validated", true).saveIt();
			if (authToken != null) {
				authToken.setLong("citizen_id", pastCitizenId).setBoolean("validated", true).saveIt();
			}
		} else {//first validation ever
			Base.exec("UPDATE votes SET validated=? WHERE citizen_id=?", true, citizen.getLongId());
			citizen.setBoolean("validated", true).saveIt();
			if (authToken != null) {
				authToken.setBoolean("validated", true).saveIt();
			}
		}
		session(Const.AUTH_CITIZEN, citizen);

		validation.set("validated", true).saveIt();//for future reference
		//update questions stats (votes/tally) for every question on which the newly-validated citizen voted..
		Base.exec("UPDATE questions q SET popular_votes=(SELECT count(*) FROM votes v WHERE v.question_id=q.id and v.validated=true) "
				+ " WHERE q.id in (SELECT distinct question_id FROM votes WHERE citizen_id=?)", citizen.getLongId());
		Base.exec("UPDATE questions q " + "SET popular_vote_tally=(SELECT count(*) FROM votes v "
				+ " WHERE v.question_id=q.id and v.validated=true and v.value=1)/q.popular_votes "
				+ " WHERE q.id in (SELECT distinct question_id FROM votes WHERE citizen_id=?)", citizen.getLongId());

		Base.commitTransaction();

		view("validatedCitizen", true);
		redirect("/home");

	}

	//////////////////////////////////

	protected void prepareHeaderValues() {
		List<Lang> langs = Lang.findAll().orderBy("code");//TODO add the languages to the app context - they are immutable and we can save a DB roundtrip
		view("langs", langs);
		List<CountedTag> tags = tagsByPubQuestionsCount();
		view("tags", tags);
		List<Country> euCountries = Country.findAll().orderBy("code");
		view("euCountries", euCountries);
		view("guessedCountry", "ro");//TODO: implement a service to guess the country based on the incoming IP (not bullet-proof but 80% accurate)
		Lang sessionLang = (Lang) session(Const.CURRENT_LANGUAGE);
		view("preferredLang", sessionLang);

		String url = StringUtils.nvl(url());
		String uri = StringUtils.nvl(uri());
		view("reqURL", url);
		view("reqURI", uri);
		int pos = url.indexOf(uri, protocol().length() + 1);
		view("reqHostname", StringUtils.nvl(pos > 0 ? url.substring(0, pos) : url));
		view("reqQuery", StringUtils.nvl(queryString()));
		view("isMobileAgent", isMobileAgent());
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
