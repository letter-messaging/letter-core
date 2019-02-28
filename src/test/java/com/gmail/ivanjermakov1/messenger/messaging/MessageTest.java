package com.gmail.ivanjermakov1.messenger.messaging;

import com.gmail.ivanjermakov1.messenger.auth.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.*;
import com.gmail.ivanjermakov1.messenger.messaging.controller.ConversationController;
import com.gmail.ivanjermakov1.messenger.messaging.controller.ImageController;
import com.gmail.ivanjermakov1.messenger.messaging.controller.MessagingController;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessageService;
import com.gmail.ivanjermakov1.messenger.messaging.util.Images;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MessageTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ConversationController conversationController;
	
	@Autowired
	private MessagingController messagingController;
	
	@Autowired
	private ImageController imageController;
	
	@Test
	public void shouldSendMessageWithImage() throws RegistrationException, AuthenticationException, NoSuchEntityException, InvalidMessageException, IOException, InvalidFileException {
		userService.register(new RegisterUserDto("Jack", "Johnson", "jackj", "password1"));
		String user1Token = userService.authenticate("jackj", "password1");
		UserDto user1 = userService.full(userService.authenticate(user1Token));
		
		userService.register(new RegisterUserDto("Ron", "Richardson", "ronr", "password1"));
		String user2Token = userService.authenticate("ronr", "password1");
		UserDto user2 = userService.full(userService.authenticate(user2Token));
		
		Assert.assertNotNull(user1);
		Assert.assertNotNull(user2);
		
		ConversationDto conversationDto = conversationController.create(user1Token, userService.getUser(user2.getId()).getLogin());
		
		NewMessageDto message = new NewMessageDto(
				user1.getId(),
				conversationDto.getId(),
				"Hello!",
				Collections.emptyList(),
				Stream
						.of(imageController.upload(
								user1Token,
								Images.multipartFileFromFile(new File("src/test/resources/test-image.jpg"))
						))
						.collect(Collectors.toList())
		);
		
		MessageDto messageDto = messagingController.sendMessage(user1Token, message);
		
		Message received = messageService.get(messageDto.getId());
		
		Assert.assertEquals(received.getImages().size(), 1);
	}
	
	
}
