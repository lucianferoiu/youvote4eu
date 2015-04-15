package app.models;

import org.javalite.activeweb.DBSpec;
import org.junit.Before;
import org.junit.Test;

import app.services.MessageDigester;
import app.services.impl.SecureMessageDigester;

public class PartnerSpec extends DBSpec {

	private MessageDigester md;

	@Before
	public void before() {
		md = new SecureMessageDigester();
	}

	@Test
	public void save() {
		Partner entity = new Partner();
		entity.setId(123L).set("email", "janedoe@example.com").set("password", md.digest("secret"));
		boolean succ = entity.insert();
		a(succ).shouldBeTrue();
		a(entity.getId()).shouldNotBeNull();
		Partner c1 = Partner.findById(123L);
		a(c1.getId()).shouldNotBeNull();
		a(c1.get("password")).shouldBeEqual(md.digest("secret"));
	}

}
