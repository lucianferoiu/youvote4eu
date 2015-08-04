package app.base;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.RowListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.util.StringUtils;
import app.util.dto.VotingEngagementDTO;
import app.util.dto.model.CountedTag;
import app.util.dto.model.FrontpageQuestion;

public abstract class QuestionsListController extends AnonAuthController {

	public static final Logger log = LoggerFactory.getLogger(QuestionsListController.class);

	protected static final String FRONTPAGE_QUESTIONS_QUERY = " SELECT q.id as qid, q.popular_votes as votes, q.open_at as pub_date, q.archived_at as arch_date, q.popular_vote_tally vote_tally, q.official_vote_tally off_vote_tally,"
			+ " q.title as en_title, tt.text as t_title, q.description as en_description, td.text as t_description "
			+ " FROM questions q "
			+ " LEFT OUTER JOIN translations tt ON (tt.parent_id=q.id AND tt.field_type='title' AND tt.lang=?) "
			+ " LEFT OUTER JOIN translations td ON (td.parent_id=q.id AND td.field_type='description' AND td.lang=?) "
			+ " WHERE q.is_deleted=false ";
	protected static final String WHERE_PUBLISHED = " AND q.is_published=true AND q.is_archived=false ";
	protected static final String WHERE_PUBLISHED_LAST30DAYS = " AND q.is_published=true AND q.is_archived=false AND q.open_at>now()-INTERVAL '30 days' ";
	protected static final String WHERE_ARCHIVED = " AND q.is_published=true AND q.is_archived=true ";
	protected static final String WHERE_TAG = " AND q.id IN (SELECT question_id FROM questions_tags WHERE tag_id=?) ";
	protected static final String WHERE_SEARCH_BY_WORD = " AND (lower(COALESCE(tt.text,q.title)) LIKE '%:word%' OR lower(COALESCE(tt.text,q.description)) LIKE '%:word%' ) ";
	protected static final String ORDER_BY_SUPPORT = " ORDER BY q.popular_votes desc, q.open_at desc ";
	protected static final String ORDER_BY_NEWNESS = " ORDER BY q.open_at desc, q.popular_votes desc ";
	protected static final String ORDER_BY_NEWNESS_ARCH = " ORDER BY q.archived_at desc NULLS FIRST, q.open_at desc, q.popular_votes desc ";
	protected static final String LIMIT = " LIMIT ";
	protected static final String OFFSET = " OFFSET ";
	//////////
	protected static final String TAGS_OF_PUB_QUESTIONS_QUERY = "SELECT t.id id, count(t.id) cnt, t.text txt FROM tags t JOIN questions_tags qt ON (t.id=qt.tag_id) "
			+ " WHERE qt.question_id IN (SELECT id FROM questions WHERE is_deleted=false AND is_published=true AND is_archived=false) "
			+ " GROUP BY t.id ORDER BY count(t.id) DESC ";
	/////////
	protected static final String QUESTION_LATEST_VOTES = "SELECT v.cast_at voted_on, v.value voted, v.validated validated, c.label cname, c.code ccode "
			+ "FROM votes v "
			+ "LEFT JOIN citizens z ON z.id=v.citizen_id "
			+ "LEFT JOIN countries c ON z.country=c.code "
			+ "WHERE v.question_id=? ORDER BY v.cast_at DESC LIMIT 10 ";

	protected List<FrontpageQuestion> findQuestions(final String lang, final boolean archived, final boolean byNewness, final String word,
			final Long tagId) {
		return findQuestions(lang, archived, byNewness, word, tagId, null, null);
	}

	protected List<FrontpageQuestion> findQuestions(final String lang, final boolean archived, final boolean byNewness, final String word,
			final Long tagId, Long offset, Long limit) {
		final List<FrontpageQuestion> questions = new ArrayList<FrontpageQuestion>();
		List<Object> params = new ArrayList<>();

		String query = FRONTPAGE_QUESTIONS_QUERY;
		params.add((StringUtils.nullOrEmpty(lang) || lang.length() != 2) ? "en" : lang.toLowerCase());
		params.add((StringUtils.nullOrEmpty(lang) || lang.length() != 2) ? "en" : lang.toLowerCase());

		if (archived) {
			query += WHERE_ARCHIVED;
		} else {
			if (byNewness) {
				query += WHERE_PUBLISHED_LAST30DAYS;
			} else {
				query += WHERE_PUBLISHED;
			}
		}

		if (tagId != null) {
			query += WHERE_TAG;
			params.add(tagId);
		}

		if (!StringUtils.nullOrEmpty(word) && word.matches("\\w+")) {
			String searchByWord = WHERE_SEARCH_BY_WORD.replaceAll(":word", word);
			query += searchByWord;
		}

		if (byNewness) {
			if (archived) {
				query += ORDER_BY_NEWNESS_ARCH;
			} else {
				query += ORDER_BY_NEWNESS;
			}
		} else {
			query += ORDER_BY_SUPPORT;
		}

		if (limit != null && limit > 0) {
			query += LIMIT;
			query += limit;
		}
		if (offset != null && offset > 0) {
			query += OFFSET;
			query += offset;
		}

		Base.find(query, params.toArray()).with(new RowListenerAdapter() {

			int i = 0;

			@Override
			public void onNext(Map<String, Object> row) {
				FrontpageQuestion fpq = new FrontpageQuestion();
				fpq.isNew = byNewness;
				fpq.isArch = archived;
				fpq.rank = i;
				fpq.id = (Long) row.get("qid");

				String en_title = (String) row.get("en_title");
				String t_title = (String) row.get("t_title");
				String en_description = (String) row.get("en_description");
				String t_description = (String) row.get("t_description");
				fpq.title = StringUtils.nullOrEmpty(t_title) ? en_title : t_title;
				fpq.description = StringUtils.nullOrEmpty(t_description) ? en_description : t_description;

				Timestamp pub_date = (Timestamp) row.get("pub_date");
				if (pub_date != null) {
					fpq.publishedOn = new Date((pub_date).toInstant().toEpochMilli());
				}
				Timestamp arch_date = (Timestamp) row.get("arch_date");
				if (arch_date != null) {
					fpq.archivedOn = new Date((arch_date).toInstant().toEpochMilli());
				}
				fpq.votesCount = (Long) row.get("votes");
				if (fpq.votesCount == null) fpq.votesCount = 0L;
				BigDecimal vTally = (BigDecimal) row.get("vote_tally");
				if (vTally != null) {
					fpq.voteTally = vTally.doubleValue();
				} else {
					fpq.voteTally = 1d;
				}
				BigDecimal offVTally = (BigDecimal) row.get("official_vote_tally");
				if (offVTally != null) {
					fpq.officialVoteTally = offVTally.doubleValue();
				} else {
					fpq.officialVoteTally = 1D;
				}

				questions.add(fpq);
				i++;
			}
		});

		log.debug("Retrieved {} {} question (tag={}, searchKeyword={})", //
				questions.size(), archived ? "archived" : "published", tagId, word);

		return questions;
	}

	protected List<CountedTag> tagsByPubQuestionsCount() {
		final List<CountedTag> tags = new ArrayList<CountedTag>();
		Base.find(TAGS_OF_PUB_QUESTIONS_QUERY, new RowListenerAdapter() {

			@Override
			public void onNext(Map<String, Object> row) {
				CountedTag tag = new CountedTag();
				tag.id = (Long) row.get("id");
				tag.count = (Long) row.get("cnt");
				tag.text = (String) row.get("txt");
				tags.add(tag);
			}
		});
		return tags;
	}

	protected List<VotingEngagementDTO> questionLatestVotes(Long qId) {
		final List<VotingEngagementDTO> stats = new ArrayList<VotingEngagementDTO>();
		Base.find(QUESTION_LATEST_VOTES, qId).with(new RowListenerAdapter() {
			@Override
			public void onNext(Map<String, Object> row) {
				VotingEngagementDTO ve = new VotingEngagementDTO();
				ve.countryCode = (String) row.get("ccode");
				ve.countryName = (String) row.get("cname");
				ve.votedOn = (Date) row.get("voted_on");
				ve.voteValue = (Integer) row.get("voted");
				ve.validated = (Boolean) row.get("validated");
				stats.add(ve);
			}
		});
		return stats;
	}

}
