package app.services;

import org.javalite.test.jspec.JSpecSupport;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.services.impl.SecureMessageDigester;

public class MessageDigesterSpec extends JSpecSupport {

	static final Logger log = LoggerFactory.getLogger(MessageDigesterSpec.class);

	private MessageDigester md;

	@Before
	public void before() {
		md = new SecureMessageDigester();
	}

	@Test
	public void mdSanityCheck() {
		String msg = "this should be good";
		String digest = md.digest(msg, false);
		a(digest).shouldNotBeNull();
		log.debug("Digest of message \"{}\" is {}", msg, digest);
	}

	@Test
	public void mdConsistency() {

		String msg = "reference message that should be identically digested every time";
		String digest = md.digest(msg, false);
		log.debug("Digest of message \"{}\" is {}", msg, digest);
		for (int i = 0; i < 12345; i++) {
			/*
			System.out.print(".");
			if (i % 200 == 0) System.out.println();
			*/
			a(digest).shouldBeEqual(md.digest(msg, false));
		}

	}

}
