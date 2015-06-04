package app.util.dto.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;

import app.models.Comment;
import app.models.Question;
import app.models.Tag;
import app.models.Translation;
import app.util.StringUtils;

/**
 * @Deprecated not used - translations are now polymorphic one-to-manys
 */
public class FullQuestion {

	public Question question;
	public Map<String, Translation> titles;
	public Map<String, Translation> descriptions;
	public Map<String, Translation> contents;
	public List<Tag> tags = Collections.emptyList();
	public List<Comment> flags = Collections.emptyList();

	public FullQuestion() {}

	public FullQuestion(Long id) {
		Question q = Question.findById(id);
		if (q != null) {
			question = q;

			//build translations
			Long titleLabelGroup = question.getLong("title_id");
			if (titleLabelGroup == null) {//new label group
				titleLabelGroup = (Long) Base.firstCell("select nextval('label_groups') ");
				question.set("title_id", titleLabelGroup);
			}
			Long descLabelGroup = question.getLong("description_id");
			if (descLabelGroup == null) {//new label group
				descLabelGroup = (Long) Base.firstCell("select nextval('label_groups') ");
				question.set("description_id", descLabelGroup);
			}
			Long contentLabelGroup = question.getLong("html_content_id");
			if (contentLabelGroup == null) {//new label group
				contentLabelGroup = (Long) Base.firstCell("select nextval('label_groups') ");
				question.set("html_content_id", contentLabelGroup);
			}

			//ensure we have a full compliment of labels
			List langs = Base.firstColumn("select code from langs");
			for (Object olg : langs) {
				String lg = olg.toString();
				//				titles.put(lg, new Translation(titleLabelGroup, lg));
				//				descriptions.put(lg, new Translation(descLabelGroup, lg));
				//				contents.put(lg, new Translation(contentLabelGroup, lg));
			}

			//retrieve the existing translations
			List<Translation> labels = null;
			labels = Translation.find("label_group=?", titleLabelGroup);
			for (Translation lb : labels) {
				String lg = lb.getString("lang");
				titles.put(lg, lb);
			}
			labels = Translation.find("label_group=?", descLabelGroup);
			for (Translation lb : labels) {
				String lg = lb.getString("lang");
				descriptions.put(lg, lb);
			}
			labels = Translation.find("label_group=?", contentLabelGroup);
			for (Translation lb : labels) {
				String lg = lb.getString("lang");
				contents.put(lg, lb);
			}

			//retrieve the tags
			String tagBitmap = q.getString("tags_bitmap");
			if (!StringUtils.nullOrEmpty(tagBitmap)) {
				q.getAll(null);
			}

		}

	}

	public boolean save() {
		boolean succ = true;

		return succ;
	}
}
