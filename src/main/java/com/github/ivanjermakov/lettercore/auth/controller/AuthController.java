package com.github.ivanjermakov.lettercore.auth.controller;

import com.github.ivanjermakov.lettercore.auth.dto.AuthUserDto;
import com.github.ivanjermakov.lettercore.auth.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.user.dto.UserDto;
import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.user.mapper.UserMapper;
import com.github.ivanjermakov.lettercore.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@Transactional
public class AuthController {

	private final UserService userService;
	private final UserMapper userMapper;

	@Autowired
	public AuthController(UserService userService, UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
	}

	/**
	 * Authenticate user by its credentials.
	 *
	 * @param user user credentials
	 * @return user authentication token
	 * @throws AuthenticationException on invalid credentials
	 */
	@GetMapping
	public String authenticate(AuthUserDto user) throws AuthenticationException {
		return userService.authenticate(user);
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
	 * Logout current user from everywhere by removing all his tokens.
	 * To login user is forced to authenticate again.
	 *
	 * @param user authenticated user. Automatically maps, when {@literal Auth-Token} parameter present
	 */
	@GetMapping("logout")
	public void logout(@ModelAttribute User user) {
		userService.logout(user);
	}

}
