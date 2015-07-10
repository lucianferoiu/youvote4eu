package app.controllers;

import app.base.AnonAuthController;
import app.util.StringUtils;

public class PreferencesController extends AnonAuthController {

	public void lang() {
		String langCode = param("langCode");
		if (!StringUtils.nullOrEmpty(langCode)) {
			setPreferredLang(langCode);
		} else {
			setPreferredLang("en");
		}
		redirect("/");
	}
}
