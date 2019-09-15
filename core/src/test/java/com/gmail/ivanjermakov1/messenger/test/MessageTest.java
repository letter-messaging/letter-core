package com.gmail.ivanjermakov1.messenger.test;

import com.gmail.ivanjermakov1.messenger.controller.ConversationController;
import com.gmail.ivanjermakov1.messenger.controller.MessageController;
import com.gmail.ivanjermakov1.messenger.controller.MessagingController;
import com.gmail.ivanjermakov1.messenger.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.service.TestingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MessageTest {

	@Autowired
	private MessageController messageController;

	@Autowired
	private ConversationController conversationController;

	@Autowired
	private MessagingController messagingController;

	@Autowired
	private TestingService testingService;

	private TestingUser user1;
	private ConversationDto conversationDto;
	private MessageDto message1;
	private MessageDto message2;

	@Before
	public void before() throws RegistrationException, AuthenticationException, AuthorizationException {
		user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		conversationDto = conversationController.create(
				user1.user,
				user2.user.login
		);

		message1 = messagingController.sendMessage(
				user1.user,
				new NewMessageDto(
						user1.user.id,
						conversationDto.id,
						"Hello!"
				)
		);

		message2 = messagingController.sendMessage(
				user1.user,
				new NewMessageDto(
						user1.user.id,
						conversationDto.id,
						"Hello2!"
				)
		);
	}

	@Test
	public void shouldGetAllMessagesFromConversation() throws AuthenticationException {
		List<MessageDto> messages = messageController.get(
				user1.user,
				conversationDto.id,
				PageRequest.of(0, Integer.MAX_VALUE)
		);

		Assert.assertEquals(2, messages.size());
	}

	@Test
	public void shouldDeleteAllMessagesFromConversation() throws AuthenticationException {
		messageController.delete(
				user1.user,
				Arrays.asList(message1, message2)
		);

		List<MessageDto> messages = messageController.get(
				user1.user,
				conversationDto.id,
				PageRequest.of(0, Integer.MAX_VALUE)
		);

		Assert.assertTrue(messages.isEmpty());
	}

}
