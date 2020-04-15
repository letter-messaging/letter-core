package com.github.ivanjermakov.lettercore.test.integration;

import com.github.ivanjermakov.lettercore.auth.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.auth.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.auth.exception.RegistrationException;
import com.github.ivanjermakov.lettercore.conversation.controller.ConversationController;
import com.github.ivanjermakov.lettercore.conversation.controller.PreviewController;
import com.github.ivanjermakov.lettercore.conversation.dto.ConversationDto;
import com.github.ivanjermakov.lettercore.conversation.dto.PreviewDto;
import com.github.ivanjermakov.lettercore.dto.TestingUser;
import com.github.ivanjermakov.lettercore.messaging.controller.MessagingController;
import com.github.ivanjermakov.lettercore.messaging.dto.NewMessageDto;
import com.github.ivanjermakov.lettercore.service.TestingService;
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
	public void shouldGet1Conversation() throws RegistrationException, AuthenticationException, AuthorizationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversation = conversationController.create(
				user1.user,
				user2.user.login
		);

//		message is required for conversation to be visible through previewController.all()
		messagingController.sendMessage(
				user1.user,
				new NewMessageDto(
						user1.user.id,
						conversation.id,
						"hello"
				)
		);

		List<PreviewDto> previews = previewController.all(user1.user, PageRequest.of(0, 1));
		Assert.assertEquals(1, previews.size());
	}

	@Test
	public void shouldGetNoConversations() throws RegistrationException, AuthenticationException {
		TestingUser user1 = testingService.registerUser("Jack");

		List<PreviewDto> previews = previewController.all(user1.user, PageRequest.of(0, 1));
		Assert.assertTrue(previews.isEmpty());
	}

}
