package app.controllers.platform;

import java.util.List;

import org.javalite.activeweb.annotations.POST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.PlatformController;
import app.models.Partner;
import app.util.StringUtils;

public class PartnersController extends PlatformController {

	protected static final int PAGE_SIZE = 10;
	public static final Logger log = LoggerFactory.getLogger(PartnersController.class);

	public void index() {
		redirect("/platform/partners/list");
	}

	public void list() {
		Integer page = 1;
		try {
			String pageParam = param("page");
			if (!StringUtils.nullOrEmpty(pageParam)) {
				page = Integer.decode(pageParam);
			}
		}
		catch (NumberFormatException nfe) {
			page = 1;
		}
		List<Partner> partners = Partner.findAll().offset((page - 1) * PAGE_SIZE).limit(PAGE_SIZE)
				.orderBy("first_login");
		Long count = Partner.count();
		partners = sanitizePartners(partners);//not really needed, since we're not JSONing the records to the browser...
		view("partners", partners);
		view("cntPartners", count);
		view("crtPage", page);
		view("cntPages", (count <= 0 ? 1 : (int) Math.ceil((double) count / PAGE_SIZE)));

	}

	private List<Partner> sanitizePartners(List<Partner> partners) {
		for (Partner p : partners) {
			p.set("password", null).set("auth_token", null);
		}
		return partners;
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
