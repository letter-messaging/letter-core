package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@Transactional
public class AuthenticationController {

	private final UserService userService;

	private final UserMapper userMapper;

	@Autowired
	public AuthenticationController(UserService userService, UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
	}

	/**
	 * Authenticate user by its credentials.
	 *
	 * @param login    user login
	 * @param password user password
	 * @return user authentication token
	 * @throws AuthenticationException on invalid credentials
	 */
	@GetMapping
	public String authenticate(@RequestParam("login") String login, @RequestParam("password") String password) throws AuthenticationException {
		return userService.authenticate(login, password);
	}

	/**
	 * Validate user token. During validation user appears online.
	 *
	 * @param token user token
	 * @return user
	 * @throws AuthenticationException on invalid token
	 */
	@GetMapping("validate")
	public UserDto validate(@RequestHeader("Auth-Token") String token) throws AuthenticationException {
		User user = userService.authenticate(token);
		userService.appearOnline(user);
		return userMapper.map(user);
	}

	/**
	 * Logout current user from everywhere by removing all his tokens. To login user is forced to authenticate again.
	 *
	 * @param token token of user
	 * @throws AuthenticationException when token is invalid
	 */
	@GetMapping("logout")
	public void logout(@RequestHeader("Auth-Token") String token) throws AuthenticationException {
		User user = userService.authenticate(token);

		userService.logout(user);
	}

}
