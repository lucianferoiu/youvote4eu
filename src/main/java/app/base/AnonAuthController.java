package app.base;

import org.javalite.activeweb.AppController;

import app.models.Citizen;
import app.models.Lang;
import app.services.TokenGenerator;
import app.util.StringUtils;

import com.google.inject.Inject;

public abstract class AnonAuthController extends AppController {

	@Inject
	protected TokenGenerator tokenGenerator;

	@Override
	protected String getLayout() {
		return "layout";
	}

	protected Lang preferredLang() {
		Lang sessionLang = (Lang) session(Const.CURRENT_LANGUAGE);
		if (sessionLang == null) {
			String lang = "en";
			Citizen citizen = (Citizen) session(Const.AUTH_CITIZEN);
			if (citizen != null) {
				String citizenLang = citizen.getString("lang");
				if (!StringUtils.nullOrEmpty(citizenLang)) {
					lang = citizenLang;
				}
			}
			sessionLang = Lang.findFirst("code=?", lang);
			if (sessionLang != null) {
				session(Const.CURRENT_LANGUAGE, sessionLang);
			}

		}
		return sessionLang;
	}

	protected String preferredLangCode() {
		Lang lang = preferredLang();
		if (lang != null) {
			String code = lang.getString("code");
			if (!StringUtils.nullOrEmpty(code)) return code;
		}
		return "en";
	}

	protected void setPreferredLang(String langCode) {
		if (!StringUtils.nullOrEmpty(langCode)) {
			Lang sessionLang = Lang.findFirst("code=?", langCode);
			if (sessionLang != null) {
				session(Const.CURRENT_LANGUAGE, sessionLang);
				Citizen citizen = (Citizen) session(Const.AUTH_CITIZEN);
				if (citizen != null) {
					citizen.set("lang", langCode).save();
				}
			}
		}
	}
}
