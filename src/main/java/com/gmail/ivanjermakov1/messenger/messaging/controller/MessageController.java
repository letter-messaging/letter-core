package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessageService;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {
	
	private final UserService userService;
	private final MessageService messageService;
	private final MessagingService messagingService;
	
	@Autowired
	public MessageController(UserService userService, MessageService messageService, MessagingService messagingService) {
		this.userService = userService;
		this.messageService = messageService;
		this.messagingService = messagingService;
	}
	
	@GetMapping("get")
	public List<MessageDto> get(@RequestHeader("Auth-Token") String token,
	                            @RequestParam("conversationId") Long conversationId,
	                            @RequestParam("offset") Integer offset,
	                            @RequestParam("amount") Integer amount) throws AuthenticationException, NoSuchEntityException {
		User user = userService.authenticate(token);
		messagingService.processConversationRead(user, conversationId);
		return messageService.get(user.getId(), conversationId, offset, amount);
	}
	
	@PostMapping("delete")
	public void delete(@RequestHeader("Auth-Token") String token, @RequestBody List<MessageDto> deleteMessages) throws AuthenticationException {
		User user = userService.authenticate(token);
		messageService.delete(user, deleteMessages);
	}
	
}
