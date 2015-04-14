package app.models;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.javalite.activeweb.DBSpec;
import org.junit.Test;

public class CitizenSpec extends DBSpec {

	@Test
	public void save() {
		Citizen entity = new Citizen();
		boolean succ = entity.save();
		a(succ).shouldBeTrue();
		a(entity.getLongId()).shouldNotBeNull();
	}

	@Test
	public void recordValidation() {
		Citizen citizen = new Citizen();
		a(citizen).shouldBe("valid");
		a(citizen.get("validated")).shouldBeNull();
		boolean saved = citizen.saveIt();
		// a(citizen.get("validated")).shouldBeFalse(); <-- it seems that PostgreSQL sets the defaults upon commit, which doesn't happen in a DBSpec (a rollback is issued)
		a(saved).shouldBeTrue();
	}

	@Test
	public void votingProcess() {
		Citizen c = new Citizen();
		c.set("validated", true);
		c.saveIt();
		Long cid = c.getLongId();

		//vote on invalid questions
		a(c.vote(-13L, 1)).shouldBeFalse();
		Question q = new Question();
		a(c.vote(q, 1)).shouldBeFalse();

		//already voted
		q = new Question();
		q.set("title_en", "already answered").saveIt();
		a(c.vote(q, 1)).shouldBeTrue();//first vote
		a(c.vote(q, 0)).shouldBeFalse();//second vote shouldn't be allowed

		Vote v = c.findVote(q.getLongId());
		a(v).shouldNotBeNull();
		a(v.get("validated")).shouldBeTrue();//inherited from the citizen

		//closed for voting
		q = new Question();
		LocalDate today = LocalDate.now();
		Date yesterday = Date.from(today.minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC));
		q.set("title_en", "closed").setDate("closed_at", yesterday).saveIt();
		a(c.vote(q, 0)).shouldBeFalse();

	}

}
