package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.EditMessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewMessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.ConversationReadAction;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.MessageEditAction;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.NewMessageAction;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.Request;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessageService;
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
	public MessagingController(MessagingService messagingService, UserService userService, MessageService messageService) {
		this.messagingService = messagingService;
		this.userService = userService;
	}
	
	@RequestMapping("get/m")
	@GetMapping
	public DeferredResult<NewMessageAction> getMessage(@RequestHeader("Auth-Token") String token) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		DeferredResult<NewMessageAction> request = new DeferredResult<>();
		request.onTimeout(() -> messagingService.getNewMessageRequests().removeIf(r -> r.getDeferredResult().equals(request)));
		messagingService.getNewMessageRequests().add(new Request<>(user, request));
		
		return request;
	}
	
	@RequestMapping("get/r")
	@GetMapping
	public DeferredResult<ConversationReadAction> getRead(@RequestHeader("Auth-Token") String token) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		DeferredResult<ConversationReadAction> request = new DeferredResult<>();
		request.onTimeout(() -> messagingService.getConversationReadRequests().removeIf(r -> r.getDeferredResult().equals(request)));
		messagingService.getConversationReadRequests().add(new Request<>(user, request));
		
		return request;
	}
	
	@RequestMapping("get/e")
	@GetMapping
	public DeferredResult<MessageEditAction> getEdit(@RequestHeader("Auth-Token") String token) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		DeferredResult<MessageEditAction> request = new DeferredResult<>();
		request.onTimeout(() -> messagingService.getMessageEditRequests().removeIf(r -> r.getDeferredResult().equals(request)));
		messagingService.getMessageEditRequests().add(new Request<>(user, request));
		
		return request;
	}
	
	@RequestMapping("send")
	@PostMapping
	public MessageDTO sendMessage(@RequestHeader("Auth-Token") String token, @RequestBody NewMessageDTO newMessageDTO) throws AuthenticationException, InvalidMessageException, NoSuchEntityException {
		User user = userService.authenticate(token);
		
		messagingService.processConversationRead(user, newMessageDTO.getConversationId());
		return messagingService.processNewMessage(user, newMessageDTO);
	}
	
	@RequestMapping("edit")
	@PostMapping
	public MessageDTO editMessage(@RequestHeader("Auth-Token") String token, @RequestBody EditMessageDTO editMessageDTO) throws AuthenticationException, InvalidMessageException, NoSuchEntityException {
		User user = userService.authenticate(token);
		
		if (editMessageDTO.getText() == null)
			throw new InvalidMessageException("invalid message");
		
		return messagingService.processMessageEdit(user, editMessageDTO);
	}
	
}
