package com.github.ivanjermakov.lettercore.mail.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	private final static Logger LOG = LoggerFactory.getLogger(MailService.class);

	@Value("${spring.mail.username}")
	private String from;

	private final JavaMailSender javaMailSender;

	@Autowired
	public MailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void send(String to, String subject, String content) {
		LOG.info("Sending message to @", to);

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		javaMailSender.send(message);

		LOG.info("Sent message to @", to);
	}

}
