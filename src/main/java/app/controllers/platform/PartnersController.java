package app.controllers.platform;

import java.util.List;

import org.javalite.activeweb.annotations.POST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.PlatformController;
import app.models.Partner;
import app.util.StringUtils;

public class PartnersController extends PlatformController {

	public static final Logger log = LoggerFactory.getLogger(PartnersController.class);

	public void index() {
		redirect("platform/partners/list");
	}

	public void list() {
		Integer page = 0;
		try {
			String pageParam = param("page");
			if (!StringUtils.nullOrEmpty(pageParam)) {
				page = Integer.decode(pageParam);
			}
		} catch (NumberFormatException nfe) {
			page = 0;
		}
		List<Partner> partners = Partner.findAll().offset(page * 10).limit(10).orderBy("first_login");
		view("partners", partners);
		view("crtPage", page);
	}

	public void edit() {
		String idParam = param("id");
		if (!StringUtils.nullOrEmpty(idParam)) {
			Long partnerId = Long.decode(idParam);
			Partner partner = Partner.findById(partnerId);
			if (partner != null) {
				view("partner", partner);
			}

		}
	}

	@POST
	public void save() {
		redirect("platform/partners/list");
	}

}
