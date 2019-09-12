package com.gmail.ivanjermakov1.messenger.messaging;

import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.controller.ConversationController;
import com.gmail.ivanjermakov1.messenger.messaging.controller.ImageController;
import com.gmail.ivanjermakov1.messenger.messaging.controller.MessagingController;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessageService;
import com.gmail.ivanjermakov1.messenger.messaging.service.TestingService;
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
	private MessageService messageService;

	@Autowired
	private ImageController imageController;

	@Autowired
	private MessagingController messagingController;

	@Autowired
	private ConversationController conversationController;

	@Autowired
	private UserService userService;

	@Autowired
	private TestingService testingService;

	@Test
	public void shouldSendMessage() throws RegistrationException, AuthenticationException, NoSuchEntityException, InvalidMessageException, AuthorizationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		Assert.assertNotNull(user1);
		Assert.assertNotNull(user2);

		ConversationDto conversationDto = conversationController.create(
				user1.token,
				userService.getUser(user2.user.id).getLogin()
		);

		NewMessageDto message = new NewMessageDto(
				user1.user.id,
				conversationDto.id,
				"Hello!",
				Collections.emptyList(),
				Collections.emptyList(),
				Collections.emptyList()
		);

		messagingController.sendMessage(user1.token, message);
	}

	@Test
	public void shouldSendMessageWithImage() throws RegistrationException, AuthenticationException, NoSuchEntityException, InvalidMessageException, IOException, AuthorizationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		Assert.assertNotNull(user1);
		Assert.assertNotNull(user2);

		ConversationDto conversationDto = conversationController.create(user1.token, userService.getUser(user2.user.id).getLogin());

		NewMessageDto message = new NewMessageDto(
				user1.user.id,
				conversationDto.id,
				"Hello!",
				Collections.emptyList(),
				Stream
						.of(imageController.upload(
								user1.token,
								Images.multipartFileFromFile(
										new File(System.getProperty("user.dir") + "/src/test/resources/test.jpg")
								)
						))
						.collect(Collectors.toList()),
				Collections.emptyList()
		);

		MessageDto messageDto = messagingController.sendMessage(user1.token, message);

		Message received = messageService.get(messageDto.id);

		Assert.assertEquals(1, received.getImages().size());
	}

}
