package com.gmail.ivanjermakov1.messenger.test.integration;

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
import org.junit.Before;
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

	private TestingUser user1;
	private TestingUser user2;
	private ConversationDto conversationDto;

	@Before
	public void before() {
		user1 = testingService.registerUser("Jack");
		user2 = testingService.registerUser("Ron");

		conversationDto = conversationController.create(
				user1.user,
				user2.user.login
		);
	}

	@Test
	public void shouldCreateConversation() throws RegistrationException, AuthenticationException, NoSuchEntityException, AuthorizationException {
		Assert.assertNotNull(conversationDto);
		Assert.assertEquals(2, conversationDto.users.size());
	}

	@Test
	public void shouldCreateSelfConversation() throws RegistrationException, AuthenticationException, NoSuchEntityException, AuthorizationException {
		ConversationDto self = conversationController.create(
				user1.user,
				user1.user.login
		);

		Assert.assertNotNull(self);
		Assert.assertEquals(1, self.users.size());
	}

	@Test
	public void shouldDeleteConversation() throws RegistrationException, AuthenticationException {
		conversationController.delete(user1.user, conversationDto.id);

		PreviewDto previewDto = previewController.get(user1.user, conversationDto.id);
		Assert.assertTrue(previewDto.conversation.hidden);
	}

	@Test
	public void shouldHideConversation() throws RegistrationException, AuthenticationException {
		conversationController.hide(user1.user, conversationDto.id);

		PreviewDto previewDto = previewController.get(user1.user, conversationDto.id);
		Assert.assertTrue(previewDto.conversation.hidden);
	}

	@Test
	public void shouldCreateHiddenConversation() {
		shouldHideConversation();

		conversationDto = conversationController.create(
				user1.user,
				user2.user.login
		);

		PreviewDto previewDto = previewController.get(user1.user, conversationDto.id);
		Assert.assertFalse(previewDto.conversation.hidden);
	}

}
