package com.gmail.ivanjermakov1.messenger.test;

import com.gmail.ivanjermakov1.messenger.controller.ConversationController;
import com.gmail.ivanjermakov1.messenger.controller.PreviewController;
import com.gmail.ivanjermakov1.messenger.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.service.TestingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
	public void shouldCreateConversation() throws RegistrationException, AuthenticationException, NoSuchEntityException, AuthorizationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversationDto = conversationController.create(
				user1.user,
				user2.user.login
		);

		Assert.assertNotNull(conversationDto);
		Assert.assertEquals(2, conversationDto.users.size());
	}

	@Test
	public void shouldCreateSelfConversation() throws RegistrationException, AuthenticationException, NoSuchEntityException, AuthorizationException {
		TestingUser user = testingService.registerUser("Jack");

		ConversationDto conversationDto = conversationController.create(
				user.user,
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
				user1.user,
				user2.user.login
		);


		conversationController.delete(user1.user, conversationDto.id);

		PreviewDto previewDto = previewController.get(user1.user, conversationDto.id);
		Assert.assertTrue(previewDto.conversation.hidden);
	}

	@Test
	public void shouldHideConversation() throws RegistrationException, AuthenticationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversationDto = conversationController.create(
				user1.user,
				user2.user.login
		);

		conversationController.hide(user1.user, conversationDto.id);

		PreviewDto previewDto = previewController.get(user1.user, conversationDto.id);
		Assert.assertTrue(previewDto.conversation.hidden);
	}

}
