package app.config;

public class FreeMarkerConfig extends
		org.javalite.activeweb.freemarker.AbstractFreeMarkerConfig {
	@Override
	public void init() {
		// this is to override a strange FreeMarker default processing of
		// numbers
		getConfiguration().setNumberFormat("0.##");
	}
}
