package com.github.ivanjermakov.lettercore.test.unit;

import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.repository.UserRepository;
import com.github.ivanjermakov.lettercore.service.HashService;
import com.github.ivanjermakov.lettercore.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserUnitTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private HashService hashService;

	@Mock
	private UserRepository userRepository;

	@Test(expected = AuthenticationException.class)
	public void shouldThrowException_WhenAuthenticatingSystemUser() throws AuthenticationException {
		User system = new User("system", "");
		system.id = 0L;

		when(hashService.check(anyString(), anyString()))
				.thenReturn(true);

		when(userRepository.findByLogin(anyString()))
				.thenReturn(Optional.of(system));

		userService.authenticate("system", "password");
	}

	@Test(expected = NoSuchEntityException.class)
	public void shouldThrowException_WhenNoSuchUserByLogin() {
		userService.getUser("");
	}

}
