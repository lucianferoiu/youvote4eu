package app.models;

import org.javalite.activeweb.DBSpec;
import org.junit.Before;
import org.junit.Test;

import app.services.TokenGenerator;
import app.services.impl.SecureRandomTokenGenerator;

public class EmailValidationSpec extends DBSpec {

	//	private MessageDigester md;
	private TokenGenerator tok;

	@Before
	public void before() {
		//		md = new SecureMessageDigester();
		tok = new SecureRandomTokenGenerator();
	}

	@Test
	public void save() {
		EmailValidation entity = new EmailValidation();
		String t = tok.generateToken();
		entity.setId(123L).set("email", "janedoe@example.com").set("token", t);
		boolean succ = entity.insert();
		a(succ).shouldBeTrue();
		a(entity.getId()).shouldNotBeNull();
		EmailValidation c1 = EmailValidation.findById(123L);
		a(c1.getId()).shouldNotBeNull();
		a(c1.get("token")).shouldBeEqual(t);
		a(c1.get("validated")).shouldBeFalse();
	}

}
