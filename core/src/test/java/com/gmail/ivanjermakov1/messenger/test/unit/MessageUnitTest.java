package com.gmail.ivanjermakov1.messenger.test.unit;

import com.gmail.ivanjermakov1.messenger.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.repository.UserConversationRepository;
import com.gmail.ivanjermakov1.messenger.service.ConversationService;
import com.gmail.ivanjermakov1.messenger.service.MessageService;
import com.gmail.ivanjermakov1.messenger.service.TestingService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@Ignore
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MessageUnitTest {

	@InjectMocks
	private MessageService messageService;

	@Mock
	private UserConversationRepository userConversationRepository;

	@Autowired
	private ConversationService conversationService;

	@Autowired
	private TestingService testingService;

	@Test(expected = NoSuchEntityException.class)
	public void shouldThrowException_WhenGetWithInvalidConversationId() throws RegistrationException, AuthenticationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");
		Conversation conversation = conversationService.create(user1.user, user2.user);

		when(userConversationRepository.findByUserAndConversation(any(), any()))
				.thenReturn(Optional.empty());

		messageService.get(user1.user.id, conversation.id, Pageable.unpaged());
	}

}
