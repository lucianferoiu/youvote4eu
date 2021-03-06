package app.controllers.platform;

import java.util.Set;

import org.javalite.activeweb.annotations.GET;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.Const;
import app.base.PlatformController;
import app.models.Lang;
import app.models.Partner;
import app.models.Tag;

/**
 * JSON-centric service for reference data retrieval
 */
public class RefController extends PlatformController {
	public static final Logger log = LoggerFactory.getLogger(RefController.class);

	@GET
	public void langs() {
		Set<String> atts = (Lang.getMetaModel().getAttributeNamesSkip("created_at", "updated_at"));
		String json = Lang.findAll().orderBy("label_en asc").toJson(true, atts.toArray(new String[0]));
		respond(json).contentType("application/json").status(200);
	}

	@GET
	public void tags() {
		Set<String> atts = (Tag.getMetaModel().getAttributeNamesSkip("created_at", "updated_at"));
		String json = Tag.findAll().orderBy("id asc").toJson(true, atts.toArray(new String[0]));
		respond(json).contentType("application/json").status(200);
	}

	@GET
	public void user() {
		Partner me = (Partner) session(Const.AUTHENTICATED_PARTNER);
		final String[] EXCLUDED_FIELDS = { "password", "auth_token", "created_at", "updated_at" };
		if (me == null) {
			log.warn("No authenticated partner on session..");
			json_404("session corrupted: no authenticated user");//this could very well be 403 - how did this call got through?!
			return;
		}
		returnJson(Partner.getMetaModel(), me, EXCLUDED_FIELDS);
	}

}
