package com.github.ivanjermakov.lettercore.messaging.integration;

import com.github.ivanjermakov.lettercore.auth.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.auth.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.auth.exception.RegistrationException;
import com.github.ivanjermakov.lettercore.common.dto.TestingUser;
import com.github.ivanjermakov.lettercore.common.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.common.service.TestingService;
import com.github.ivanjermakov.lettercore.conversation.controller.ConversationController;
import com.github.ivanjermakov.lettercore.conversation.dto.ConversationDto;
import com.github.ivanjermakov.lettercore.messaging.controller.MessagingController;
import com.github.ivanjermakov.lettercore.messaging.dto.EditMessageDto;
import com.github.ivanjermakov.lettercore.messaging.dto.MessageDto;
import com.github.ivanjermakov.lettercore.messaging.dto.NewMessageDto;
import com.github.ivanjermakov.lettercore.messaging.dto.action.Action;
import com.github.ivanjermakov.lettercore.messaging.dto.action.NewMessageAction;
import com.github.ivanjermakov.lettercore.messaging.entity.Message;
import com.github.ivanjermakov.lettercore.messaging.service.MessageService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
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
