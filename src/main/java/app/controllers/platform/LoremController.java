package app.controllers.platform;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.javalite.activejdbc.Base;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.PlatformController;
import app.models.Comment;
import app.models.Lang;
import app.models.Partner;
import app.models.Question;
import app.models.QuestionsTags;
import app.models.Translation;
import app.models.Vote;
import app.util.StringUtils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class LoremController extends PlatformController {

	static final Logger log = LoggerFactory.getLogger(LoremController.class);
	static final Random random = new Random();

	public void generatePartners() {

		String buildEnv = System.getenv("BUILD_ENV");
		if (StringUtils.nullOrEmpty(buildEnv) || ((!"dev".equalsIgnoreCase(buildEnv)) && (!"staging".equalsIgnoreCase(buildEnv)))) {
			json_403();
			return;
		}

		String countParam = param("count");
		if (StringUtils.nullOrEmpty(countParam)) {
			json_400("count param required");
			return;
		}
		Integer howMany = Integer.parseInt(countParam);
		log.debug("Generating {} partner accounts", howMany);
		/////////////////////////////////////////////
		Base.openTransaction();
		for (int i = 0; i < howMany; i++) {
			Partner randomPartner = new Partner();
			randomPartner.set("email", String.format("partner%03d@example.com", i));
			randomPartner.set("name", String.format("Random Partner %03d", i));
			randomPartner.setBoolean("verified", random.nextBoolean());
			randomPartner.setBoolean("is_organization", random.nextBoolean());
			randomPartner.setBoolean("enabled", random.nextBoolean());
			randomPartner.setBoolean("can_add_question", random.nextBoolean());
			randomPartner.setBoolean("can_edit_own_question", random.nextBoolean());
			randomPartner.setBoolean("can_edit_any_question", random.nextBoolean());
			randomPartner.setBoolean("can_archive_any_question", random.nextBoolean());
			randomPartner.setBoolean("can_change_translation", random.nextBoolean());
			randomPartner.setBoolean("can_approve_question", random.nextBoolean());
			randomPartner.setBoolean("can_view_statistics", random.nextBoolean());

			randomPartner.saveIt();
		}
		Base.commitTransaction();
		/////////////////////////////////////////////
		json_201(howMany + " partners created");
	}

	public void generateQuestions() {
		String buildEnv = System.getenv("BUILD_ENV");
		if (StringUtils.nullOrEmpty(buildEnv) || ((!"dev".equalsIgnoreCase(buildEnv)) && (!"staging".equalsIgnoreCase(buildEnv)))) {
			json_403();
			return;
		}

		String countParam = param("count");
		if (StringUtils.nullOrEmpty(countParam)) {
			json_400("count param required");
			return;
		}
		Integer howMany = Integer.parseInt(countParam);
		log.debug("Generating {} random questions in various states", howMany);
		Base.openTransaction();

		//we need:
		//	- howMany * 28 short sentences
		//	- howMany * 28 long (double) sentences or short paragraphs
		//	- howMany * 28 * 5 paragraphs
		try {

			//get some Lorem Ipsum texts
			HttpResponse<JsonNode> response = null;
			response = Unirest
					.get("https://montanaflynn-lorem-text-generator.p.mashape.com/sentence?count=" + (howMany * 28) + "&length=7")
					.header("X-Mashape-Key", "kfMBHsWeCbmsh7KOSgQ9iFbm7Mizp1naHIqjsnA7lkE5COmTzI").header("Accept", "application/json")
					.asJson();
			JSONArray titles = response.getBody().getArray();
			log.debug("Retrieved {} Lorem titles", titles.length());

			response = Unirest
					.get("https://montanaflynn-lorem-text-generator.p.mashape.com/paragraph?count=" + (howMany * 28 * 2) + "&length=3")
					.header("X-Mashape-Key", "kfMBHsWeCbmsh7KOSgQ9iFbm7Mizp1naHIqjsnA7lkE5COmTzI").header("Accept", "application/json")
					.asJson();
			JSONArray descriptions = response.getBody().getArray();
			log.debug("Retrieved {} Lorem descriptions", descriptions.length());

			response = Unirest
					.get("https://montanaflynn-lorem-text-generator.p.mashape.com/paragraph?count=" + (howMany * 28 * 5) + "&length=7")
					.header("X-Mashape-Key", "kfMBHsWeCbmsh7KOSgQ9iFbm7Mizp1naHIqjsnA7lkE5COmTzI").header("Accept", "application/json")
					.asJson();
			JSONArray htmlContents = response.getBody().getArray();
			log.debug("Retrieved {} Lorem contents", htmlContents.length());

			//comments
			response = Unirest.get("https://montanaflynn-lorem-text-generator.p.mashape.com/sentence?count=" + (howMany * 7) + "&length=5")
					.header("X-Mashape-Key", "kfMBHsWeCbmsh7KOSgQ9iFbm7Mizp1naHIqjsnA7lkE5COmTzI").header("Accept", "application/json")
					.asJson();
			JSONArray comments = response.getBody().getArray();
			log.debug("Retrieved {} Lorem comments", comments.length());

			//languages
			List<Lang> langs = Lang.findAll();

			//partner IDs
			List partners = Base.firstColumn("select id from partners");
			int partnersCount = partners.size();

			//tags
			List tags = Base.firstColumn("select id from tags");
			int tagsCount = tags.size();

			//comment types
			String[] cTypes = { "promoted", "duplicate", "offensive", "mistranslated", "misleading", "broken link", "comment", "comment",
					"comment", "comment" };

			int t = 0, d = 0, h = 0, c = 0;
			Date now = new Date();
			long nowMillis = now.getTime();
			long dayMillis = 24 * 60 * 60 * 1000;
			long aYearAgoMillis = nowMillis - (365 * dayMillis);

			for (int i = 0; i < howMany; i++) {
				Question q = new Question();
				Long popularVotes = null;
				Double popularVoteTally = null;
				q.set("title", toQuestion(titles.get(t++)));
				q.set("description", descriptions.get(d++));
				String text = htmlContents.get(h++).toString();
				int r1 = random.nextInt(3);
				for (int r = 0; r < r1; r++) {
					text = text + " <br/> <br/> " + htmlContents.get(h++).toString();
				}
				q.set("html_content", text);

				q.setLong("proposed_by", partners.get(random.nextInt(partnersCount - 1)));
				q.setBoolean("is_public_agenda", random.nextBoolean());
				q.setBoolean("is_published", random.nextBoolean());//~ half are published
				q.setBoolean("is_archived", q.getBoolean("is_published") && (random.nextBoolean() || random.nextBoolean()));// .. and ~ three quarters are archived
				q.setLong("support",
						(random.nextInt(3) * (random.nextInt(11) * random.nextInt(17)) + random.nextInt(7)) + random.nextInt(23));
				if (q.getBoolean("is_published")) {
					popularVotes = new Long(random.nextInt(3) * (random.nextInt(37) * random.nextInt(17)) + random.nextInt(19))
							+ random.nextInt(79);
					q.setLong("popular_votes", popularVotes);
					if (popularVotes > 0) {
						popularVoteTally = new Double((3 + random.nextInt(90)) / 100.0D);
						q.setDouble("popular_vote_tally", popularVoteTally);

					}
					long openMillis = nextLong(aYearAgoMillis, nowMillis - (random.nextInt(150) * dayMillis));
					long closedMillis = nextLong(Math.min(openMillis + (5 * dayMillis), openMillis + (random.nextInt(50) * dayMillis)),
							nowMillis);
					long archMillis = nextLong(Math.min(closedMillis + (2 * dayMillis), nowMillis - 1000), nowMillis);
					q.setTimestamp("open_at", new Date(openMillis));
					q.setTimestamp("closed_at", new Date(closedMillis));//at least 5 days opened..
					if (q.getBoolean("is_archived")) {
						q.setTimestamp("archived_at", new Date(archMillis));
						q.set("archival_conclusion", descriptions.get(d++));
					}
				}

				if (q.getBoolean("is_archived") && q.getBoolean("is_public_agenda")) {
					q.setDouble("official_vote_tally", (7 + random.nextInt(70)) / 100.0D);
					if (random.nextInt(3) == 1) {//%33 chances of..
						q.setDouble("parliament_vote_tally", (7 + random.nextInt(70)) / 100.0D);
						q.set("parliament_vote_link", "http://www.europarl.europa.eu/plenary/en/votes.html?tab=votes");
						q.setTimestamp("parliament_voted_on", new Date(nextLong(aYearAgoMillis, nowMillis)));
					}
					if (random.nextInt(3) == 1) {//%33 chances of..
						q.setDouble("council_vote_tally", (7 + random.nextInt(70)) / 100.0D);
						q.set("council_vote_link",
								"http://www.consilium.europa.eu/register/en/content/out/?PUB_DOC=%3E0&RESULTSET=1&DOC_SUBJECT=VOTE&i=VT&ROWSPP=25&DOC_LANCD=EN&ORDERBY=DOC_DATE%20DESC&typ=SET&NRROWS=500&DOC_YEAR=2015");
						q.setTimestamp("council_voted_on", new Date(nextLong(aYearAgoMillis, nowMillis)));
					}
					if (random.nextInt(3) == 1) {//%33 chances of..
						q.setBoolean("commission_decision", random.nextBoolean());
						q.set("commission_decision_link", "http://eur-lex.europa.eu/oj/direct-access.html");
						q.setTimestamp("commission_decided_on", new Date(nextLong(aYearAgoMillis, nowMillis)));
					}

				}

				q.saveIt();
				Long qId = q.getLongId();

				if (popularVotes != null && popularVoteTally != null && popularVotes > 0 && popularVoteTally > 0) {
					//create fake votes
					int yesVotes = (int) Math.round(popularVotes * popularVoteTally);
					for (int k = 0; k < yesVotes; k++) {
						Vote.createIt("question_id", qId, "value", 1, "validated", true, "citizen_id", -2L);
					}
					for (int k = yesVotes; k < popularVotes; k++) {
						Vote.createIt("question_id", qId, "value", 0, "validated", true, "citizen_id", -3L);
					}
				}

				//translations
				for (Lang lang : langs) {
					String lg = lang.getString("code");
					if (!"en".equalsIgnoreCase(lg)) {
						if (random.nextInt(100) < 30) continue;//~30% chaches of skipping this language..
						Translation tt = new Translation();
						tt.set("text", toQuestion(titles.get(t++)));
						tt.set("field_type", "title");
						tt.set("lang", lg);
						q.add(tt);
						Translation td = new Translation();
						td.set("text", descriptions.get(d++));
						td.set("field_type", "description");
						td.set("lang", lg);
						q.add(td);
						Translation tc = new Translation();
						String tct = htmlContents.get(h++).toString();
						int r2 = random.nextInt(4);
						for (int r = 0; r < r2; r++) {
							tct = tct + " <br/> <br/> " + htmlContents.get(h++).toString();
						}
						tc.set("text", tct);
						tc.set("field_type", "html_content");
						tc.set("lang", lg);
						q.add(tc);
					}
				}

				//tags
				int r3 = random.nextInt(4);
				for (int j = 0; j < r3; j++) {
					if (random.nextInt(100) < 40) continue;//~40% chaches of skip tagging ..
					QuestionsTags qt = QuestionsTags.createIt("created_by", partners.get(random.nextInt(partnersCount - 1)), "question_id",
							qId, "tag_id", tags.get(random.nextInt(tagsCount - 1)));
				}

				//comments
				int r4 = random.nextInt(6);
				for (int j = 0; j < r4; j++) {
					if (random.nextInt(100) < 40) continue;//~40% chaches of skipping this comment..
					Comment cm = new Comment();
					cm.set("created_by", partners.get(random.nextInt(partnersCount - 1)));
					cm.set("comment_type", cTypes[random.nextInt(cTypes.length - 1)]);
					cm.set("text", comments.get(c++));
					q.add(cm);
				}
				q.saveIt();
			}

		}
		catch (UnirestException e) {
			log.debug("Cannot retrieve lorem words", e);
			Base.rollbackTransaction();
			json_500("Cannot retrieve so many lorem paragraphs..");
			return;
		}
		catch (Exception e) {
			log.debug("Unexpected exception", e);
			Base.rollbackTransaction();
			json_500("Cannot generate questions..");
			return;
		}

		Base.commitTransaction();
		json_201(howMany + " questions created");
	}

	private String toQuestion(Object t) {
		String txt = "";
		if (t != null) {
			txt = t.toString();
			txt = txt.trim().replace('.', '?');
		}
		return txt;
	}

	//////////////////////////////////////////////////////////////////

	private long nextLong(long from, long to) {
		if (from > to) {
			long t = from;
			from = to;
			to = t;
		}
		//get the range, casting to long to avoid overflow problems
		long range = to - from + 1;
		// compute a fraction of the range, 0 <= frac < range
		long fraction = (long) (range * random.nextDouble());
		long randomNumber = fraction + from;
		return randomNumber;
	}

	private double getGaussian(double aMean, double aVariance) {
		return aMean + random.nextGaussian() * aVariance;
	}

}
