package com.gmail.ivanjermakov1.messenger.messaging;

import com.gmail.ivanjermakov1.messenger.auth.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.core.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.controller.ConversationController;
import com.gmail.ivanjermakov1.messenger.messaging.controller.MessagingController;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewMessageDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MessagingIntegrationTest {
	
	@Autowired
	private MessagingController messagingController;
	
	@Autowired
	private ConversationController conversationController;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMapper userMapper;
	
	@Test
	public void shouldSendAndReceiveMessage() throws RegistrationException, AuthenticationException, NoSuchEntityException, InvalidMessageException {
		userService.register(new RegisterUserDto("Jack", "Johnson", "jackj", "password1"));
		String user1Token = userService.authenticate("jackj", "password1");
		UserDto user1 = userMapper.map(userService.authenticate(user1Token));
		
		userService.register(new RegisterUserDto("Ron", "Richardson", "ronr", "password1"));
		String user2Token = userService.authenticate("ronr", "password1");
		UserDto user2 = userMapper.map(userService.authenticate(user2Token));
		
		Assert.assertNotNull(user1);
		Assert.assertNotNull(user2);
		
		ConversationDto conversationDto = conversationController.create(user1Token, userService.getUser(user2.getId()).getLogin());
		
		NewMessageDto message = new NewMessageDto(
				user1.getId(),
				conversationDto.getId(),
				"Hello!",
				Collections.emptyList(),
				Collections.emptyList(),
				Collections.emptyList()
		);
		
		messagingController.sendMessage(user1Token, message);
	}
	
}
