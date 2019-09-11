package com.gmail.ivanjermakov1.messenger.messaging;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidSearchFormatException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.messaging.service.SearchService;
import com.gmail.ivanjermakov1.messenger.messaging.service.TestingService;
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
	private SearchService searchService;

	@Autowired
	private TestingService testingService;

	@Test
	public void shouldFindUserByLogin() throws RegistrationException, AuthenticationException, InvalidSearchFormatException {
		TestingUser user = testingService.registerUser("John");

		List<UserDto> searchResult = searchService.searchUsers("@John", PageRequest.of(0, Integer.MAX_VALUE));
		Assert.assertTrue(searchResult
				.stream()
				.anyMatch(dto -> dto.getLogin().equals(user.user.getLogin()))
		);
	}

	@Test(expected = InvalidSearchFormatException.class)
	public void shouldThrowInvalidSearchFormatException_WithInvalidSearch() throws RegistrationException, AuthenticationException, InvalidSearchFormatException {
		TestingUser user = testingService.registerUser("John");

		searchService.searchUsers("John", PageRequest.of(0, Integer.MAX_VALUE));
	}

}
