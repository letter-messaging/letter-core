package com.github.ivanjermakov.lettercore.service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

	private final static Logger LOG = LoggerFactory.getLogger(SmsServiceImpl.class);

	@Value("${twilio.account.sid}")
	private String accountSid;

	@Value("${twilio.auth.token}")
	private String token;

	@Value("${twilio.from.number}")
	private String from;

	@Override
	public void send(String to, String content) {
		LOG.debug("sending sms to @" + to);

		Twilio.init(accountSid, token);
		Message message = Message.creator(new PhoneNumber(from), new PhoneNumber(to), content).create();

		LOG.info("sent sms to @" + to);
	}

}
