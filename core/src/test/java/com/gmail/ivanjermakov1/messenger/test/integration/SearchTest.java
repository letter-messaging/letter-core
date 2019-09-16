package com.gmail.ivanjermakov1.messenger.test.integration;

import com.gmail.ivanjermakov1.messenger.controller.ConversationController;
import com.gmail.ivanjermakov1.messenger.controller.MessagingController;
import com.gmail.ivanjermakov1.messenger.controller.SearchController;
import com.gmail.ivanjermakov1.messenger.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidSearchFormatException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.service.TestingService;
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
	public void shouldThrowException_WithInvalidSearch() throws RegistrationException, AuthenticationException, InvalidSearchFormatException {
		TestingUser user = testingService.registerUser("John");

		searchController.searchUsers(
				user.user,
				"John",
				PageRequest.of(0, Integer.MAX_VALUE)
		);
	}

	@Test
	public void shouldFindConversationByFirstName() throws RegistrationException, AuthenticationException {
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

//		message is required for conversation to be visible through previewController.all()
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
	}

}
