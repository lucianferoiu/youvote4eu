package app.controllers.platform;

import org.javalite.activeweb.annotations.GET;
import org.javalite.activeweb.annotations.POST;
import org.javalite.activeweb.annotations.PUT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.PlatformController;
import app.models.Partner;
import app.util.StringUtils;

public class PartnersController extends PlatformController {

	private static final Logger log = LoggerFactory.getLogger(PartnersController.class);
	protected static final int PAGE_SIZE = 10;
	protected static final String[] EXCLUDED_FIELDS = { "password", "auth_token", "created_at", "updated_at" };

	/**
	 * Render AngularJS SPA (the rest of the methods are JSON-based)
	 * 
	 * @see /views/platform/partners/index.ftl
	 * @see /app/platform/partners/*.js etc.
	 */
	public void index() {}

	@GET
	public void list() {
		returnJsonResults(Partner.getMetaModel(), Partner.findAll(), Partner.count(), EXCLUDED_FIELDS);
	}

	@GET
	public void edit() {
		String idParam = param("id");
		try {
			if (!StringUtils.nullOrEmpty(idParam)) {
				Long partnerId = Long.decode(idParam);
				Partner partner = Partner.findById(partnerId);
				returnJson(partner, EXCLUDED_FIELDS);
			}
		}
		catch (NumberFormatException nfe) {
			json_404("no partner found with id: " + idParam);
		}

	}

	@POST
	public void save() {
		redirect("/platform/partners/list");
	}

	@PUT
	public void add() {
		redirect("/platform/partners/list");
	}

}
