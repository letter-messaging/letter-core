package com.gmail.ivanjermakov1.messenger.test.unit;

import com.gmail.ivanjermakov1.messenger.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.entity.Image;
import com.gmail.ivanjermakov1.messenger.entity.Message;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.validator.MessageValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MessageValidatorUnitTest {

	@Autowired
	private MessageValidator validator;
	private Message nullMessage;
	private Message message;

	@Before
	public void before() {
		nullMessage = new Message(
				null,
				null,
				null,
				null,
				null,
				null,
				null
		);

		User user = new User(
				"login",
				"hash"
		);
		user.id = 1L;

		message = new Message(
				new Conversation(
						null,
						new ArrayList<>(),
						user
				),
				LocalDateTime.now(),
				null,
				user,
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>()
		);
		message.conversation.id = 1L;
	}

	@Test
	public void shouldNotValidateNullMessage() {
		boolean isValid = validator.validate(null);
		assertFalse(isValid);
	}

	@Test
	public void shouldNotValidateMessageOfNull() {
		boolean isValid = validator.validate(nullMessage);
		assertFalse(isValid);
	}

	@Test
	public void shouldNotValidateMessageWithoutTextAndAttachments() {
		boolean isValid = validator.validate(message);
		assertFalse(isValid);
	}

	@Test
	public void shouldValidateMessageWithTextAndWithoutAttachments() {
		message.text = "hello";
		boolean isValid = validator.validate(message);
		assertTrue(isValid);
	}

	@Test
	public void shouldValidateMessageWithoutTextAndWithAttachments() {
		message.images.add(new Image(
				message.sender,
				message,
				"path",
				LocalDate.now()
		));
		boolean isValid = validator.validate(message);
		assertTrue(isValid);
	}

	@Test
	public void shouldValidateMessageWithTextAndWithAttachments() {
		message.images.add(new Image(
				message.sender,
				message,
				"path",
				LocalDate.now()
		));
		message.text = "hello";
		boolean isValid = validator.validate(message);
		assertTrue(isValid);
	}

}
