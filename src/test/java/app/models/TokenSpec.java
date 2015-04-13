package app.models;

import org.javalite.activeweb.DBSpec;
import org.junit.Test;

public class TokenSpec extends DBSpec {
	
	@Test
	public void dummyRecordValidation() {
		Token tok = new Token();
		a(tok.get("validated")).shouldBeNull();
	}
	
	@Test
	public void retrieveCitizenFromToken() {
		final String tok="tok1";
		final Long citizenId=1234L;
		//setup
		Citizen c1 = new Citizen();
		c1.setId(citizenId);
		a(c1.insert()).shouldBeTrue();
		Token t1 = new Token();
		t1.set("token","tok1");
		c1.add(t1);
		a(c1.save()).shouldBeTrue();
		
		//lookup
		Token t2 = Token.findFirst("token=?", tok);
		a(t2).shouldNotBeNull();
		Citizen c2 = t2.parent(Citizen.class);
		a(c2).shouldNotBeNull();
		a(c2.getId()).shouldBeEqual(citizenId);
	}
	
	@Test
	public void validateCitizenViaToken() {
		final Long citizenId=1234L;
		//setup
		Citizen c1 = Citizen.create("validated",false);
		c1.setId(citizenId);
		a(c1.insert()).shouldBeTrue();
		Token t1 = new Token();
		c1.add(t1);
		a(c1.save()).shouldBeTrue();

		//check
		a(c1.get("validated")).shouldBeFalse();
		t1.validateToken();
		c1.refresh();
		a(c1.get("validated")).shouldBeTrue();
		
	}

}
