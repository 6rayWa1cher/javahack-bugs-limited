package com.a6raywa1cher.javahackbugslimited.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

	public final JavaMailSender emailSender;

	@Autowired
	public EmailService(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	public void sendSimpleMessage(
			String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}
}