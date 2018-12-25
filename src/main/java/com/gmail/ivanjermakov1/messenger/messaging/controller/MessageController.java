package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDTO;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserMainInfo;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessageService;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
	
	@GetMapping("init/full")
	public MessageDTO initFull() {
		return new MessageDTO(new Message(), new UserDTO(new User(), new UserMainInfo()), new Conversation(), Collections.emptyList());
	}
	
	@GetMapping("get")
	public List<MessageDTO> get(@RequestHeader("Auth-Token") String token,
	                            @RequestParam("conversationId") Long conversationId,
	                            @RequestParam("offset") Integer offset,
	                            @RequestParam("amount") Integer amount) throws AuthenticationException, NoSuchEntityException {
		User user = userService.auth(token);
		messagingService.processConversationRead(user, conversationId);
		return messageService.get(user.getId(), conversationId, offset, amount);
	}
	
	@PostMapping("delete")
	public void delete(@RequestHeader("Auth-Token") String token, @RequestBody List<MessageDTO> deleteMessages) throws AuthenticationException {
		User user = userService.auth(token);
		messageService.delete(user, deleteMessages);
	}
	
}
