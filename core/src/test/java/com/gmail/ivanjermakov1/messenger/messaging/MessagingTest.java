package com.gmail.ivanjermakov1.messenger.messaging;

import com.gmail.ivanjermakov1.messenger.controller.ConversationController;
import com.gmail.ivanjermakov1.messenger.controller.MessagingController;
import com.gmail.ivanjermakov1.messenger.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.dto.EditMessageDto;
import com.gmail.ivanjermakov1.messenger.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.dto.action.Action;
import com.gmail.ivanjermakov1.messenger.dto.action.NewMessageAction;
import com.gmail.ivanjermakov1.messenger.entity.Message;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.messaging.service.TestingService;
import com.gmail.ivanjermakov1.messenger.service.MessageService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
@RunWith(SpringRunner.class)
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
	public void shouldSendMessage() throws RegistrationException, AuthenticationException, NoSuchEntityException, InvalidMessageException, AuthorizationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversationDto = conversationController.create(
				user1.token,
				user2.user.login
		);

		NewMessageDto message = new NewMessageDto(
				user1.user.id,
				conversationDto.id,
				"Hello!"
		);

		MessageDto messageDto = messagingController.sendMessage(user1.token, message);

		Assert.assertNotNull(messageDto);
		Assert.assertNotNull(messageDto.id);
	}

	@Test
	public void shouldEditMessage() throws RegistrationException, AuthenticationException, AuthorizationException, InvalidMessageException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversationDto = conversationController.create(
				user1.token,
				user2.user.login
		);

		NewMessageDto newMessage = new NewMessageDto(
				user1.user.id,
				conversationDto.id,
				"Hello!"
		);

		MessageDto message = messagingController.sendMessage(user1.token, newMessage);

		MessageDto editMessage = messagingController.editMessage(
				user1.token,
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
	public void shouldReceiveMessage() throws RegistrationException, AuthenticationException, InvalidMessageException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversationDto = conversationController.create(
				user1.token,
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

		MessageDto messageDto = messagingController.sendMessage(user1.token, message);
		Assert.assertNotNull(messageDto);
		Assert.assertNotNull(messageDto.id);
	}

}
