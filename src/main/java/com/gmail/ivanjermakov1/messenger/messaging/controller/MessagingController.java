package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.EditMessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

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
	
	@GetMapping("listen")
	public ResponseBodyEmitter getEvents(@RequestParam("token") String token) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		return messagingService.generateRequest(user);
	}
	
	@PostMapping("send")
	public MessageDto sendMessage(@RequestHeader("Auth-Token") String token, @RequestBody NewMessageDto newMessageDto) throws AuthenticationException, InvalidMessageException, NoSuchEntityException {
		User user = userService.authenticate(token);
		
		return messagingService.processNewMessage(user, newMessageDto);
	}
	
	@PostMapping("edit")
	public MessageDto editMessage(@RequestHeader("Auth-Token") String token, @RequestBody EditMessageDto editMessageDto) throws AuthenticationException, InvalidMessageException, NoSuchEntityException {
		User user = userService.authenticate(token);
		
		if (editMessageDto.getText() == null)
			throw new InvalidMessageException("invalid message");
		
		return messagingService.processMessageEdit(user, editMessageDto);
	}
	
}
