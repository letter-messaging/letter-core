package com.github.ivanjermakov.lettercore.test.integration;

import com.github.ivanjermakov.lettercore.controller.ConversationController;
import com.github.ivanjermakov.lettercore.controller.MessagingController;
import com.github.ivanjermakov.lettercore.dto.ConversationDto;
import com.github.ivanjermakov.lettercore.dto.EditMessageDto;
import com.github.ivanjermakov.lettercore.dto.MessageDto;
import com.github.ivanjermakov.lettercore.dto.NewMessageDto;
import com.github.ivanjermakov.lettercore.dto.TestingUser;
import com.github.ivanjermakov.lettercore.dto.action.Action;
import com.github.ivanjermakov.lettercore.dto.action.NewMessageAction;
import com.github.ivanjermakov.lettercore.entity.Message;
import com.github.ivanjermakov.lettercore.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.exception.RegistrationException;
import com.github.ivanjermakov.lettercore.service.MessageService;
import com.github.ivanjermakov.lettercore.service.TestingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
public class MessagingTest {

	@Autowired
	private MessageService messageService;

	@Autowired
	private MessagingController messagingController;

	@Autowired
	private ConversationController conversationController;

	@Autowired
	private TestingService testingService;

	@Test
	public void shouldSendMessage() throws RegistrationException, AuthenticationException, NoSuchEntityException, AuthorizationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversationDto = conversationController.create(
				user1.user,
				user2.user.login
		);

		NewMessageDto message = new NewMessageDto(
				user1.user.id,
				conversationDto.id,
				"Hello!"
		);

		MessageDto messageDto = messagingController.sendMessage(user1.user, message);

		Assert.assertNotNull(messageDto);
		Assert.assertNotNull(messageDto.id);
	}

	@Test
	public void shouldEditMessage() throws RegistrationException, AuthenticationException, AuthorizationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversationDto = conversationController.create(
				user1.user,
				user2.user.login
		);

		NewMessageDto newMessage = new NewMessageDto(
				user1.user.id,
				conversationDto.id,
				"Hello!"
		);

		MessageDto message = messagingController.sendMessage(user1.user, newMessage);

		MessageDto editMessage = messagingController.editMessage(
				user1.user,
				new EditMessageDto(
						message.id,
						"Edit"
				)
		);

		Assert.assertNotNull(editMessage);
		Assert.assertEquals(message.id, editMessage.id);
		Assert.assertEquals("Edit", editMessage.text);

		Message receivedEditMessage = messageService.get(message.id);

		Assert.assertNotNull(receivedEditMessage);
		Assert.assertEquals(editMessage.id, receivedEditMessage.id);
		Assert.assertEquals(editMessage.text, receivedEditMessage.text);
	}

	@Test
	public void shouldReceiveMessage() throws RegistrationException, AuthenticationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversationDto = conversationController.create(
				user1.user,
				user2.user.login
		);

		Flux<Action> events = messagingController.getEvents(
				user1.token
		);

		NewMessageDto message = new NewMessageDto(
				user1.user.id,
				conversationDto.id,
				"Hello!"
		);

		new Thread(() -> StepVerifier
				.create(events)
				.expectNextMatches(a -> ((NewMessageAction) a).message.text.equals(message.text))
				.thenCancel()
//				if event will never emit - exception will be thrown after 1 second, without duration param in this case
//				thread will suspend forever
				.verify(Duration.ofSeconds(1))
		).start();

		MessageDto messageDto = messagingController.sendMessage(user1.user, message);
		Assert.assertNotNull(messageDto);
		Assert.assertNotNull(messageDto.id);
	}

	@Test
	public void shouldCloseSseConnection() {
		TestingUser user1 = testingService.registerUser("Jack");

		Flux<Action> events = messagingController.getEvents(
				user1.token
		);

		StepVerifier
				.create(events)
				.expectComplete()
				.verify(Duration.ofMillis(100L));
	}

}
