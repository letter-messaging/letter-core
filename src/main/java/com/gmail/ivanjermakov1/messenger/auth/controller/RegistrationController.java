package com.gmail.ivanjermakov1.messenger.auth.controller;

import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegistrationController {
	
	private final UserService userService;
	
	@Autowired
	public RegistrationController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public void register(@RequestParam("firstName") String firstName,
	                     @RequestParam("lastName") String lastName,
	                     @RequestParam("login") String login,
	                     @RequestParam("password") String password) throws RegistrationException {
		userService.register(firstName, lastName, login, password);
	}
	
}
