package com.gmail.ivanjermakov1.messenger.messaging;

import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.controller.ConversationController;
import com.gmail.ivanjermakov1.messenger.messaging.controller.PreviewController;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.messaging.service.TestingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class ConversationTest {

	@Autowired
	private ConversationController conversationController;

	@Autowired
	private PreviewController previewController;

	@Autowired
	private TestingService testingService;

	@Test
	public void shouldCreateConversation() throws RegistrationException, AuthenticationException, NoSuchEntityException, InvalidMessageException, AuthorizationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversationDto = conversationController.create(
				user1.token,
				user2.user.login
		);

		Assert.assertNotNull(conversationDto);
		Assert.assertEquals(2, conversationDto.users.size());
	}

	@Test
	public void shouldCreateSelfConversation() throws RegistrationException, AuthenticationException, NoSuchEntityException, InvalidMessageException, IOException, AuthorizationException {
		TestingUser user = testingService.registerUser("Jack");

		ConversationDto conversationDto = conversationController.create(
				user.token,
				user.user.login
		);

		Assert.assertNotNull(conversationDto);
		Assert.assertEquals(1, conversationDto.users.size());
	}

	@Test
	public void shouldDeleteConversation() throws RegistrationException, AuthenticationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversationDto = conversationController.create(
				user1.token,
				user2.user.login
		);


		conversationController.delete(user1.token, conversationDto.id);

		PreviewDto previewDto = previewController.get(user1.token, conversationDto.id);
		Assert.assertTrue(previewDto.conversation.hidden);
	}

	@Test
	public void shouldHideConversation() throws RegistrationException, AuthenticationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversationDto = conversationController.create(
				user1.token,
				user2.user.login
		);

		conversationController.hide(user1.token, conversationDto.id);

		PreviewDto previewDto = previewController.get(user1.token, conversationDto.id);
		Assert.assertTrue(previewDto.conversation.hidden);
	}

}
