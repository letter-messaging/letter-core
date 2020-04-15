package com.github.ivanjermakov.lettercore.test.unit;

import com.github.ivanjermakov.lettercore.dto.NewChatDto;
import com.github.ivanjermakov.lettercore.dto.TestingUser;
import com.github.ivanjermakov.lettercore.entity.Conversation;
import com.github.ivanjermakov.lettercore.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.exception.RegistrationException;
import com.github.ivanjermakov.lettercore.service.ChatService;
import com.github.ivanjermakov.lettercore.service.TestingService;
import com.github.ivanjermakov.lettercore.service.UserService;
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
