package app.services;

import org.javalite.test.jspec.JSpecSupport;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.services.impl.SecureRandomTokenGenerator;

public class TokenGeneratorSpec extends JSpecSupport {

	static final Logger log = LoggerFactory.getLogger(TokenGeneratorSpec.class);

	private TokenGenerator tokGen;

	@Before
	public void before() {
		tokGen = new SecureRandomTokenGenerator();
	}

	@Test
	public void tokenSanityCheck() {
		String tok = tokGen.generateToken();
		a(tok).shouldNotBeNull();
		log.debug("Generated random token {}", tok);
		a(tok.length() >= 32).shouldBeTrue();
		a(tok.length() <= 36).shouldBeTrue();
		String tok2 = tokGen.generateToken();
		a(tok2).shouldNotBeNull();
		a(tok).shouldNotBeEqual(tok2);
	}

}
