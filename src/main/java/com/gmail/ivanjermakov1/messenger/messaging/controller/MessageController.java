package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.dto.UserDTO;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserMainInfo;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {
	
	private final UserService userService;
	private final MessageService messageService;
	
	
	@Autowired
	public MessageController(UserService userService, MessageService messageService) {
		this.userService = userService;
		this.messageService = messageService;
	}
	
	@GetMapping("init")
	public Message init() {
		return new Message();
	}
	
	@GetMapping("init/full")
	public MessageDTO initFull() {
		return new MessageDTO(new Message(), new UserDTO(new User(), new UserMainInfo()), new Conversation());
	}
	
	@GetMapping("get")
	public List<MessageDTO> get(@RequestParam("token") String token,
	                            @RequestParam("conversationId") Long conversationId,
	                            @RequestParam("offset") Integer offset,
	                            @RequestParam("amount") Integer amount) throws AuthenticationException {
		try {
			User user = userService.getUser(userService.getUserId(token));
			
			return messageService.get(user.getId(), conversationId, offset, amount);
		} catch (NoSuchEntityException e) {
			throw new AuthenticationException("invalid token");
		}
	}
	
}
