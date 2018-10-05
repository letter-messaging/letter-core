package com.gmail.ivanjermakov1.messenger.auth.controller;

import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
}
