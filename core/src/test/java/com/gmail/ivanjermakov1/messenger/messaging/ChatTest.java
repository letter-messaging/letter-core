package com.gmail.ivanjermakov1.messenger.messaging;

import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.controller.ChatController;
import com.gmail.ivanjermakov1.messenger.messaging.controller.MessagingController;
import com.gmail.ivanjermakov1.messenger.messaging.controller.PreviewController;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewChatDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.messaging.service.TestingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class ChatTest {

	@Autowired
	private PreviewController previewController;

	@Autowired
	private ChatController chatController;

	@Autowired
	private MessagingController messagingController;

	@Autowired
	private TestingService testingService;

	private TestingUser user1;
	private TestingUser user2;
	private ConversationDto chat;
	private MessageDto message1;
	private MessageDto message2;

	@Before
	public void before() throws RegistrationException, AuthenticationException, AuthorizationException, InvalidMessageException {
		user1 = testingService.registerUser("Jack");
		user2 = testingService.registerUser("Ron");

		chat = chatController.create(
				user1.token,
				new NewChatDto(
						"chat",
						new ArrayList<>(Collections.singletonList(user2.user.id))
				)
		);

		message1 = messagingController.sendMessage(
				user1.token,
				new NewMessageDto(
						user1.user.id,
						chat.id,
						"Hello!"
				)
		);

		message2 = messagingController.sendMessage(
				user2.token,
				new NewMessageDto(
						user2.user.id,
						chat.id,
						"Hello2!"
				)
		);
	}

	@Test
	public void shouldCreateChat() {
		Assert.assertNotNull(chat);
		Assert.assertEquals(2, chat.users.size());
	}

	@Test
	public void shouldAddMember() throws RegistrationException, AuthenticationException {
		TestingUser user3 = testingService.registerUser("John");

		chatController.addMember(
				user1.token,
				chat.id,
				user3.user.id
		);

		ConversationDto conversation = previewController.get(
				user1.token,
				chat.id
		).conversation;

		Assert.assertEquals(3, conversation.users.size());
	}

	@Test(expected = NoSuchEntityException.class)
	public void shouldThrowException_WhenNoSuchMemberAddingMember() throws AuthenticationException {
		chatController.addMember(
				user1.token,
				chat.id,
				-1L
		);
	}

	@Test(expected = NoSuchEntityException.class)
	public void shouldThrowException_WhenNoSuchChatAddingMember() throws AuthenticationException, RegistrationException {
		TestingUser user3 = testingService.registerUser("John");

		chatController.addMember(
				user1.token,
				-1L,
				user3.user.id
		);
	}

	@Test
	public void shouldAddMembers() throws RegistrationException, AuthenticationException {
		TestingUser user3 = testingService.registerUser("John");
		TestingUser user4 = testingService.registerUser("Bob");

		chatController.addMembers(
				user1.token,
				chat.id,
				Stream
						.of(user3, user4)
						.map(u -> u.user.id)
						.collect(Collectors.toList())
		);

		ConversationDto conversation = previewController.get(
				user1.token,
				chat.id
		).conversation;

		Assert.assertEquals(4, conversation.users.size());
	}

	@Test(expected = NoSuchEntityException.class)
	public void shouldThrowException_WhenNoSuchMemberAddingMembers() throws AuthenticationException {
		chatController.addMembers(
				user1.token,
				chat.id,
				new ArrayList<>(Collections.singletonList(-1L))
		);
	}

	@Test(expected = NoSuchEntityException.class)
	public void shouldThrowException_WhenNoSuchChatAddingMembers() throws AuthenticationException, RegistrationException {
		TestingUser user3 = testingService.registerUser("John");

		chatController.addMembers(
				user1.token,
				-1L,
				new ArrayList<>(Collections.singletonList(user3.user.id))
		);
	}

	@Test
	public void shouldKickMember() throws AuthenticationException {
		chatController.kickMember(
				user1.token,
				chat.id,
				user2.user.id
		);

		ConversationDto conversation = previewController.get(
				user1.token,
				chat.id
		).conversation;

		Assert.assertEquals(1, conversation.users.size());
	}

	@Test(expected = NoSuchEntityException.class)
	public void shouldThrowException_WhenNoSuchChatKickingMember() throws AuthenticationException, RegistrationException {
		TestingUser user3 = testingService.registerUser("John");

		chatController.kickMember(
				user1.token,
				-1L,
				user3.user.id
		);
	}

	@Test
	public void shouldHideChat() throws AuthenticationException {
		chatController.hide(
				user1.token,
				chat.id
		);

		ConversationDto conversationForUser1 = previewController.get(
				user1.token,
				chat.id
		).conversation;

		Assert.assertNotNull(conversationForUser1);
		Assert.assertTrue(conversationForUser1.hidden);

		ConversationDto conversationForUser2 = previewController.get(
				user2.token,
				chat.id
		).conversation;

		Assert.assertNotNull(conversationForUser2);
		Assert.assertFalse(conversationForUser2.hidden);

		Assert.assertEquals(conversationForUser1.id, conversationForUser2.id);
	}

	@Test
	public void shouldDeleteChat() throws AuthenticationException {
		chatController.delete(
				user1.token,
				chat.id
		);

		ConversationDto conversationForUser1 = previewController.get(
				user1.token,
				chat.id
		).conversation;

		Assert.assertNotNull(conversationForUser1);
		Assert.assertTrue(conversationForUser1.hidden);

		ConversationDto conversationForUser2 = previewController.get(
				user2.token,
				chat.id
		).conversation;

		Assert.assertNotNull(conversationForUser2);
		Assert.assertFalse(conversationForUser2.hidden);

		Assert.assertEquals(conversationForUser1.id, conversationForUser2.id);
	}

	@Ignore
	@Test
	public void shouldLeave() {
//      TODO
	}

}
