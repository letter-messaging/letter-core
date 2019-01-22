package com.gmail.ivanjermakov1.messenger.messaging;

import com.gmail.ivanjermakov1.messenger.auth.dto.RegisterUserDTO;
import com.gmail.ivanjermakov1.messenger.auth.dto.UserDTO;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidSearchFormatException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.service.SearchService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
	private UserService userService;
	
	private String registerUser(String firstName, String lastName, String login) throws RegistrationException, AuthenticationException {
		userService.register(new RegisterUserDTO(firstName, lastName, login, "password1"));
		return userService.authenticate(login, "password1");
	}
	
	@Test
	public void shouldFindUserByLogin() throws RegistrationException, AuthenticationException, InvalidSearchFormatException {
		String user1Token = registerUser("John", "Lens", "johnls");
		User user1 = userService.authenticate(user1Token);
		
		List<UserDTO> searchResult = searchService.searchUsers("@John");
		System.out.println("SIZE: " + searchResult.size());
		Assert.assertTrue(searchResult
				.stream()
				.anyMatch(dto -> dto.getLogin().equals(user1.getLogin()))
		);
	}
	
	@Test(expected = InvalidSearchFormatException.class)
	public void shouldThrowInvalidSearchFormatException_WithInvalidSearch() throws RegistrationException, AuthenticationException, InvalidSearchFormatException {
		registerUser("John", "Lens", "johnls");
		
		searchService.searchUsers("John");
	}
	
}
