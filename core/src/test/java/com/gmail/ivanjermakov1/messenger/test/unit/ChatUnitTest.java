package com.gmail.ivanjermakov1.messenger.test.unit;

import com.gmail.ivanjermakov1.messenger.dto.NewChatDto;
import com.gmail.ivanjermakov1.messenger.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.service.ChatService;
import com.gmail.ivanjermakov1.messenger.service.TestingService;
import com.gmail.ivanjermakov1.messenger.service.UserService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Ignore
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class ChatUnitTest {

	@Autowired
	private UserService userService;

	@Autowired
	private ChatService chatService;

	@Autowired
	private TestingService testingService;
	private TestingUser user1;
	private TestingUser user2;
	private Conversation chat;

	@Before
	public void before() throws RegistrationException, AuthenticationException {
		user1 = testingService.registerUser("Jack");
		user2 = testingService.registerUser("Ron");

		chat = chatService.create(
				user1.user,
				new NewChatDto(
						"chat",
						Collections.singletonList(user2.user.id)
				)
		);
	}

}
