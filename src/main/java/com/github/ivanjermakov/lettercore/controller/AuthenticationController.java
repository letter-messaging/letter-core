package com.github.ivanjermakov.lettercore.controller;

import com.github.ivanjermakov.lettercore.dto.UserDto;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.mapper.UserMapper;
import com.github.ivanjermakov.lettercore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
//	TODO: use AuthUserDto
	@GetMapping
	public String authenticate(@RequestParam("login") String login, @RequestParam("password") String password) throws AuthenticationException {
		return userService.authenticate(login, password);
	}

	/**
	 * Validate user token. During validation user appears online.
	 *
	 * @param user  authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param token user token
	 * @return user
	 * @throws AuthenticationException on invalid token
	 */
	@GetMapping("validate")
	public UserDto validate(@ModelAttribute User user,
	                        @RequestHeader("Auth-Token") String token) throws AuthenticationException {
		userService.authenticate(token);
		userService.appearOnline(user);
		return userMapper.map(user);
	}

	/**
	 * Logout current user from everywhere by removing all his tokens. To login user is forced to authenticate again.
	 *
	 * @param user authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 */
	@GetMapping("logout")
	public void logout(@ModelAttribute User user) {
		userService.logout(user);
	}

}
