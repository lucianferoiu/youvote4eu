package app.services;

public interface Mailer {

	boolean sendMail(String to, String subject, String content, boolean asHTML);

}
