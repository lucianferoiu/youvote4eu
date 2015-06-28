package app.base;

import java.util.ArrayList;
import java.util.List;

import app.util.dto.model.FrontpageQuestion;

public abstract class QuestionsListController extends AnonAuthController {

	protected static final String FRONTPAGE_QUESTIONS_QUERY = " SELECT q.id, q.popular_votes as votes, open_at as pub_date,"
			+ " q.title as en_title, tt.text as t_title, q.description as en_description, td.text as t_description " + " FROM questions q "
			+ " LEFT OUTER JOIN translations tt ON (tt.parent_id=q.id AND tt.field_type='title' AND tt.lang=?) "
			+ " LEFT OUTER JOIN translations td ON (td.parent_id=q.id AND td.field_type='description' AND td.lang=?) "
			+ " WHERE q.is_deleted=false ";
	protected static final String WHERE_PUBLISHED = " AND q.is_published=true AND q.is_archived=false ";
	protected static final String WHERE_ARCHIVED = " AND q.is_published=true AND q.is_archived=true ";
	protected static final String WHERE_TAG = " AND q.id IN (SELECT question_id FROM questions_tags WHERE tag_id=?) ";
	protected static final String WHERE_SEARCH_BY_WORD = " AND (lower(nvl(t_title,en_title)) LIKE '%:word%' OR lower(nvl(t_description,en_description)) LIKE '%:word%' ) ";
	protected static final String ORDER_BY_SUPPORT = " ORDER BY q.popular_votes desc, q.open_at desc ";
	protected static final String ORDER_BY_NEWNESS = " ORDER BY q.open_at desc, q.popular_votes desc ";

	protected List<FrontpageQuestion> findQuestions(boolean archived, boolean byNewness, String word, Long tagId) {
		List<FrontpageQuestion> questions = new ArrayList<FrontpageQuestion>();

		return questions;
	}

}
