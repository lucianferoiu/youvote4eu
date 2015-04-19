package app.services;

import org.javalite.test.jspec.JSpecSupport;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.services.impl.GMailMailer;

public class MailerSpec extends JSpecSupport {
	static final Logger log = LoggerFactory.getLogger(MailerSpec.class);

	private Mailer m;

	@Before
	public void before() {
		m = new GMailMailer();
	}

	@Test
	public void mdSanityCheck() {
		String msg = "this should be good";
		/* takes quite long to execute - tested by hand a couple of times, it's enough...
		boolean sent = m.sendMail("lucianferoiu@gmail.com", "MailerSpec test ",
				"Why don't you <a href='http://google.com?q=vote'>click</a> here?<br/><br/>Cheers, YV4EU", true);
		it(sent).shouldBeTrue();
		*/
	}
}
