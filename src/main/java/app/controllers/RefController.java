package app.controllers;

import java.util.List;

import org.javalite.activeweb.annotations.GET;

import app.base.AnonAuthController;
import app.models.Comment;
import app.models.Lang;
import app.models.Tag;

/**
 * JSON-centric service for reference data retrieval
 */
public class RefController extends AnonAuthController {

	@GET
	public void langs() {
		List<String> atts = (Lang.getMetaModel().getAttributeNamesSkip("created_at", "updated_at"));
		String json = Lang.findAll().orderBy("label_en asc").toJson(true, atts.toArray(new String[0]));
		respond(json).contentType("application/json").status(200);
	}

	@GET
	public void tags() {
		List<String> atts = (Tag.getMetaModel().getAttributeNamesSkip("created_at", "updated_at"));
		String json = Tag.findAll().orderBy("id asc").toJson(true, atts.toArray(new String[0]));
		respond(json).contentType("application/json").status(200);
	}

	@GET
	public void flags() {
		List<String> atts = (Comment.getMetaModel().getAttributeNamesSkip("created_at", "updated_at"));
		String json = Comment.findAll().orderBy("id asc").toJson(true, atts.toArray(new String[0]));
		respond(json).contentType("application/json").status(200);
	}

}
