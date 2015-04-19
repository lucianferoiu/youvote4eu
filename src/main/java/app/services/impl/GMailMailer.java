package app.services.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.services.Mailer;

/**
 * Dumb implementation of the Mailer using a throw-away GMail account. To be
 * re-implemented using the platform SMTP services.
 */
public class GMailMailer implements Mailer {
	static final Logger log = LoggerFactory.getLogger(GMailMailer.class);
	protected static final String MAIL_ADDRESS = "youvote4eu@gmail.com";
	private String password = null;

	@Override
	public boolean sendMail(String to, String subject, String content, boolean asHTML) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(MAIL_ADDRESS, getPassword());
			}
		});

		try {

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(MAIL_ADDRESS));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			if (asHTML) {
				message.setText(content, "UTF-8", "html");
			} else {
				message.setText(content, "UTF-8");
			}
			Transport.send(message);

			log.debug("Sent email w/ subject \"{}\" to \"{}\" ", subject, to);
		}
		catch (MessagingException e) {
			log.error("Cannot send email message", e);
			return false;
		}

		return true;
	}

	protected String getPassword() {
		if (password == null) {
			password = System.getenv("MAIL_PWD");
			//log.debug("Email password retrieved from the env: {}", password);
		}
		return password;
	}

}
