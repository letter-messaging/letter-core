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
	public UserDTO validate(@RequestHeader("Auth-Token") String token) throws NoSuchEntityException {
		User user = userService.getUserByToken(token);
		userService.appearOnline(user);
		return userService.full(user);
	}
	
}
