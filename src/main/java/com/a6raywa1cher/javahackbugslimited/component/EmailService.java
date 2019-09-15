package com.a6raywa1cher.javahackbugslimited.component;

import com.a6raywa1cher.javahackbugslimited.config.EmailConfigProperties;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
	private Mailer mailer;
	private String from;

	public EmailService(EmailConfigProperties properties) {
		this.from = properties.getFullEmail();
		this.mailer = MailerBuilder
				.withTransportStrategy(TransportStrategy.SMTPS)
				.withDebugLogging(true)
				.withSMTPServer(properties.getHost(), properties.getPort(),
						properties.getUsername(), properties.getPassword())
				.buildMailer();
	}

	public void sendSimpleMessage(
			String to, String subject, String text) {
		Email email = EmailBuilder.startingBlank()
				.from("noreply", from)
				.to(to)
				.withSubject(subject)
				.withPlainText(text)
				.buildEmail();
		mailer.sendMail(email);
	}
}