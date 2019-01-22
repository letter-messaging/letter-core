package com.gmail.ivanjermakov1.messenger.auth.controller;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDTO;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
	
	private final UserService userService;
	
	@Autowired
	public AuthenticationController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public String authenticate(@RequestParam("login") String login, @RequestParam("password") String password) throws AuthenticationException {
		return userService.authenticate(login, password);
	}
	
	@GetMapping("validate")
	public UserDTO validate(@RequestHeader("Auth-Token") String token) throws AuthenticationException {
		try {
			User user = userService.getUserByToken(token);
			userService.appearOnline(user);
			return userService.full(user);
		} catch (NoSuchEntityException e) {
			throw new AuthenticationException("invalid token");
		}
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
