package app.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.services.Mailer;
import app.util.StringUtils;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

/**
 * Heroku SendGrid mail add-on
 */
public class SendGridMailer implements Mailer {
	static final Logger log = LoggerFactory.getLogger(SendGridMailer.class);

	@Override
	public boolean sendMail(String to, String subject, String content, boolean asHTML) {
		String sendgrid_username = System.getenv("SENDGRID_USERNAME");
		String sendgrid_password = System.getenv("SENDGRID_PASSWORD");
		if (StringUtils.nullOrEmpty(sendgrid_password) || StringUtils.nullOrEmpty(sendgrid_username)) {
			log.warn("SendGrid mailer credentials are not present in env (!)");
			return false;
		}

		SendGrid sendgrid = new SendGrid(sendgrid_username, sendgrid_password);

		SendGrid.Email email = new SendGrid.Email();
		email.addTo(to);
		email.setFrom("no-reply@youvotefor.eu");
		email.setSubject(subject);
		if (asHTML) {
			email.setHtml(content);
		} else {
			email.setText(content);
		}

		try {
			SendGrid.Response response = sendgrid.send(email);
			if (response != null) {
				log.debug("Sent email to {} - response status={}, message={}", to, response.getStatus(), response.getMessage());
				return response.getStatus();
			} else {
				log.warn("Cannot send email to {} - no(null) response", to);
			}
		}
		catch (SendGridException e) {
			log.warn("Cannot send email to {} w/ subject {}: {}", to, subject, e.getMessage());
		}

		return false;
	}

}
