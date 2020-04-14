package com.gmail.ivanjermakov1.messenger.test.unit;

import com.gmail.ivanjermakov1.messenger.config.AuthenticationControllerAdvice;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class ControllerAdviceUnitTest {

	@InjectMocks
	private AuthenticationControllerAdvice authenticationControllerAdvice;

	@Mock
	private UserService userService;

	@Test
	public void shouldAuthenticateUser() {
		when(userService.authenticate(anyString()))
				.thenReturn(new User("Jack", ""));

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader(anyString())).thenReturn(anyString());

		User user = authenticationControllerAdvice.authenticate(request);
		Assert.assertNotNull(user);
		Assert.assertEquals("Jack", user.login);
	}

	@Test
	public void shouldReturnNullWhenNoTokenProvided() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader(anyString())).thenReturn(null);

		authenticationControllerAdvice.authenticate(request);
	}

	@Test(expected = AuthenticationException.class)
	public void shouldThrowException_WhenNotAuthenticateUser() {
		when(userService.authenticate(anyString()))
				.thenThrow(new AuthenticationException());

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader(anyString()))
				.thenReturn(anyString());

		authenticationControllerAdvice.authenticate(request);
	}

}
