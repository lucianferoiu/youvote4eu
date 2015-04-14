package app.controllers;

import org.javalite.activeweb.ControllerSpec;
import org.junit.Before;
import org.junit.Test;

import app.services.TokenModule;

import com.google.inject.Guice;

public class HomeControllerSpec extends ControllerSpec {
	@Before
	public void before() {
		setInjector(Guice.createInjector(new TokenModule()));
	}

	@Test
	public void shouldReportCookie() {
		long maxLen = 0;
		for (int i = 0; i < 100000; i++) {
			long before = System.currentTimeMillis();
			request().get("index");
			Object tok = val("token");
			long after = System.currentTimeMillis();
			a(tok).shouldNotBeNull();
			//System.out.println(String.format("%d - Token generated: %s   - len: %d", i, tok, tok.toString().length()));
			maxLen = Math.max(maxLen, after - before);
		}
		System.out.println(String.format("Longest it took: %d millis ", maxLen));
	}
}
