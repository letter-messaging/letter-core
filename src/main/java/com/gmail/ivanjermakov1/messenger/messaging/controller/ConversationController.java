package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConversationController {
	
	private final ConversationService conversationService;
	private final UserService userService;
	
	@Autowired
	public ConversationController(ConversationService conversationService, UserService userService) {
		this.conversationService = conversationService;
		this.userService = userService;
	}
	
	@GetMapping("conversation/create")
	public Long create(@RequestParam(name = "token") String token, @RequestParam(name = "with") String withLogin) throws AuthenticationException {
		try {
			User user = userService.getUser(userService.getUserId(token));
			return conversationService.create(user, withLogin);
		} catch (NoSuchEntityException e) {
			throw new AuthenticationException("invalid token");
		}
	}
	
}
