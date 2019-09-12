package com.gmail.ivanjermakov1.messenger.messaging;

import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.controller.ConversationController;
import com.gmail.ivanjermakov1.messenger.messaging.controller.MessagingController;
import com.gmail.ivanjermakov1.messenger.messaging.controller.PreviewController;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.messaging.service.TestingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PreviewTest {

	@Autowired
	private ConversationController conversationController;

	@Autowired
	private PreviewController previewController;

	@Autowired
	private MessagingController messagingController;

	@Autowired
	private TestingService testingService;

	@Test
	public void shouldGet1Conversation() throws RegistrationException, AuthenticationException, AuthorizationException, InvalidMessageException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversation = conversationController.create(
				user1.token,
				user2.user.login
		);

//		message is required for conversation to be visible through previewController.all()
		messagingController.sendMessage(
				user1.token,
				new NewMessageDto(
						user1.user.id,
						conversation.id,
						"hello"
				)
		);

		List<PreviewDto> previews = previewController.all(user1.token, PageRequest.of(0, 1));
		Assert.assertEquals(1, previews.size());
	}

	@Test
	public void shouldGetNoConversations() throws RegistrationException, AuthenticationException {
		TestingUser user1 = testingService.registerUser("Jack");

		List<PreviewDto> previews = previewController.all(user1.token, PageRequest.of(0, 1));
		Assert.assertTrue(previews.isEmpty());
	}

}
