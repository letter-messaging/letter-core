package com.github.ivanjermakov.lettercore.test.integration;

import com.github.ivanjermakov.lettercore.controller.ConversationController;
import com.github.ivanjermakov.lettercore.controller.MessagingController;
import com.github.ivanjermakov.lettercore.controller.SearchController;
import com.github.ivanjermakov.lettercore.dto.ConversationDto;
import com.github.ivanjermakov.lettercore.dto.NewMessageDto;
import com.github.ivanjermakov.lettercore.dto.PreviewDto;
import com.github.ivanjermakov.lettercore.dto.TestingUser;
import com.github.ivanjermakov.lettercore.dto.UserDto;
import com.github.ivanjermakov.lettercore.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.exception.InvalidSearchFormatException;
import com.github.ivanjermakov.lettercore.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.exception.RegistrationException;
import com.github.ivanjermakov.lettercore.service.TestingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class SearchTest {

	@Autowired
	private SearchController searchController;

	@Autowired
	private ConversationController conversationController;

	@Autowired
	private MessagingController messagingController;

	@Autowired
	private TestingService testingService;

	@Test
	public void shouldFindUserByLogin() throws RegistrationException, AuthenticationException, InvalidSearchFormatException {
		TestingUser user = testingService.registerUser("John");

		List<UserDto> searchResult = searchController.searchUsers(
				user.user,
				"@John",
				PageRequest.of(0, Integer.MAX_VALUE)
		);

		Assert.assertTrue(searchResult
				.stream()
				.anyMatch(dto -> dto.login.equals(user.user.login))
		);
	}

	@Test(expected = InvalidSearchFormatException.class)
	public void shouldThrowException_WithInvalidUserSearch() throws RegistrationException, AuthenticationException, InvalidSearchFormatException {
		TestingUser user = testingService.registerUser("John");

		searchController.searchUsers(
				user.user,
				"John",
				PageRequest.of(0, Integer.MAX_VALUE)
		);
	}

	@Test(expected = InvalidEntityException.class)
	public void shouldFindConversation() throws RegistrationException, AuthenticationException {
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

//		at least one message is required for conversation to be visible
		messagingController.sendMessage(user1.user, message);

		List<PreviewDto> previews = searchController.searchConversations(
				user1.user,
				user2.userDto.lastName,
				PageRequest.of(0, Integer.MAX_VALUE)
		);

		Assert.assertEquals(1, previews.size());
		Assert.assertEquals(
				conversationDto.id,
				previews
						.stream()
						.findFirst()
						.orElseThrow(NoSuchEntityException::new)
						.conversation.id
		);

		Assert.assertEquals(
				1,
				searchController
						.searchConversations(
								user1.user,
								"ron",
								PageRequest.of(0, Integer.MAX_VALUE)
						)
						.size()
		);

		Assert.assertEquals(
				1,
				searchController
						.searchConversations(
								user1.user,
								"ro",
								PageRequest.of(0, Integer.MAX_VALUE)
						)
						.size()
		);

		Assert.assertEquals(
				1,
				searchController
						.searchConversations(
								user1.user,
								"ron",
								PageRequest.of(0, Integer.MAX_VALUE)
						)
						.size()
		);

		Assert.assertEquals(
				0,
				searchController
						.searchConversations(
								user1.user,
								"abcdef",
								PageRequest.of(0, Integer.MAX_VALUE)
						)
						.size()
		);

		searchController
				.searchConversations(
						user1.user,
						"",
						PageRequest.of(0, Integer.MAX_VALUE)
				);
	}

}
