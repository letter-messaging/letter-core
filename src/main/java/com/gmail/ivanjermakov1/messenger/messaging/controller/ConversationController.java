package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("conversation")
public class ConversationController {
	
	private final ConversationService conversationService;
	private final UserService userService;
	
	@Autowired
	public ConversationController(ConversationService conversationService, UserService userService) {
		this.conversationService = conversationService;
		this.userService = userService;
	}
	
	@GetMapping("create")
	public Conversation create(@RequestHeader("Auth-Token") String token, @RequestParam("with") String withLogin) throws AuthenticationException {
		User user = userService.auth(token);
		try {
			return conversationService.create(user, userService.getUser(withLogin));
		} catch (NoSuchEntityException e) {
			throw new AuthenticationException();
		}
	}
	
}
