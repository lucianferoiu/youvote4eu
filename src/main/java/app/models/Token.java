package app.models;

import org.javalite.activejdbc.Model;

/**
 * Persistent authentication token used to track the anonymous citizen
 */
public class Token extends Model {

	/**
	 * Sets the validation flag and cascades the validation to the referred citizen
	 * @return success of the validation
	 */
	public boolean validateToken() {
		boolean success = true;
		set("validated", true);
		Citizen citizen = parent(Citizen.class);
		if (citizen != null) {
			if (citizen.getBoolean("validated")) {
				//log.info: "Citizen already valid"
			} else {
				citizen.set("validated", true);
				success = success & citizen.save();
			}
		} else {}
		success = success & save();
		
		return success;
	}
}
