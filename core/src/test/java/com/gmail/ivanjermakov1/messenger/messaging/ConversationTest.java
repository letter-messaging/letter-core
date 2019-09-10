package com.gmail.ivanjermakov1.messenger.messaging;

import com.gmail.ivanjermakov1.messenger.auth.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.core.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.controller.ConversationController;
import com.gmail.ivanjermakov1.messenger.messaging.controller.ImageController;
import com.gmail.ivanjermakov1.messenger.messaging.controller.MessagingController;
import com.gmail.ivanjermakov1.messenger.messaging.controller.PreviewController;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessageService;
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
	private ImageController imageController;

	@Autowired
	private MessagingController messagingController;

	@Autowired
	private ConversationController conversationController;

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PreviewController previewController;

	@Test
	public void shouldCreateConversation() throws RegistrationException, AuthenticationException, NoSuchEntityException, InvalidMessageException, AuthorizationException {
		userService.register(new RegisterUserDto("Jack", "Johnson", "jackj", "password1"));
		String user1Token = userService.authenticate("jackj", "password1");
		UserDto user1 = userMapper.map(userService.authenticate(user1Token));

		userService.register(new RegisterUserDto("Ron", "Richardson", "ronr", "password1"));
		String user2Token = userService.authenticate("ronr", "password1");
		UserDto user2 = userMapper.map(userService.authenticate(user2Token));

		Assert.assertNotNull(user1);
		Assert.assertNotNull(user2);

		ConversationDto conversationDto = conversationController.create(user1Token, userService.getUser(user2.getId()).getLogin());

		Assert.assertNotNull(conversationDto);
		Assert.assertEquals(2, conversationDto.getUsers().size());
	}

	@Test
	public void shouldCreateSelfConversation() throws RegistrationException, AuthenticationException, NoSuchEntityException, InvalidMessageException, IOException, AuthorizationException {
		userService.register(new RegisterUserDto("Jack", "Johnson", "jackj", "password1"));
		String user1Token = userService.authenticate("jackj", "password1");
		UserDto user = userMapper.map(userService.authenticate(user1Token));

		Assert.assertNotNull(user);

		ConversationDto conversationDto = conversationController.create(user1Token, userService.getUser(user.getId()).getLogin());

		Assert.assertNotNull(conversationDto);
		Assert.assertEquals(1, conversationDto.getUsers().size());
	}

	@Test
	public void shouldDeleteConversation() throws RegistrationException, AuthenticationException {
		userService.register(new RegisterUserDto("Jack", "Johnson", "jackj", "password1"));
		String user1Token = userService.authenticate("jackj", "password1");
		UserDto user = userMapper.map(userService.authenticate(user1Token));

		Assert.assertNotNull(user);

		ConversationDto conversationDto = conversationController.create(user1Token, userService.getUser(user.getId()).getLogin());

		Assert.assertNotNull(previewController.get(user1Token, conversationDto.getId()));

		conversationController.delete(user1Token, conversationDto.getId());

		PreviewDto previewDto = previewController.get(user1Token, conversationDto.getId());
		Assert.assertTrue(previewDto.getConversation().getHidden());
	}

}
