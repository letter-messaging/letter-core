package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.entity.action.Action;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("messaging")
public class MessagingController {
	
	private final MessagingService messagingService;
	private final UserService userService;
	
	@Autowired
	public MessagingController(MessagingService messagingService, UserService userService) {
		this.messagingService = messagingService;
		this.userService = userService;
	}
	
	@RequestMapping("get")
	@GetMapping
	public DeferredResult<Action> get(@RequestParam("token") String token) throws AuthenticationException {
		User user = userService.auth(token);
		
		DeferredResult<Action> request = new DeferredResult<>();
		request.onTimeout(() -> messagingService.removeRequest(request));
		messagingService.addRequest(user, request);
		
		return request;
	}
	
	@RequestMapping("send")
	@PostMapping
	public void sendMessage(@RequestParam("token") String token, @RequestBody Message message) throws AuthenticationException, InvalidMessageException {
		if (message.getText() == null || message.getConversationId() == null)
			throw new InvalidMessageException("invalid message");
		
		User user = userService.auth(token);
		messagingService.process(user, message);
	}
	
	
}
