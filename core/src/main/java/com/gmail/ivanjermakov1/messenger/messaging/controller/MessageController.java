package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessageService;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	/**
	 * Get list of messages.
	 *
	 * @param token          user token
	 * @param conversationId conversation id to get messages within
	 * @param offset         offset
	 * @param amount         amount
	 * @return list of messages. Return empty list on empty conversation
	 * @throws AuthenticationException on invalid @param token
	 * @throws NoSuchEntityException   on invalid @param conversationId and on missing conversation with such id
	 */
	@GetMapping("get")
	public List<MessageDto> get(@RequestHeader("Auth-Token") String token,
	                            @RequestParam("conversationId") Long conversationId,
	                            @RequestParam("offset") Integer offset,
	                            @RequestParam("amount") Integer amount) throws AuthenticationException, NoSuchEntityException {
		User user = userService.authenticate(token);
		messagingService.processConversationRead(user, conversationId);
		return messageService.get(user.getId(), conversationId, offset, amount);
	}
	
	/**
	 * Delete list of messages.
	 * It is possible to delete only user-self messages. If in list are messages from another user they will be ignored.
	 *
	 * @param token          user token
	 * @param deleteMessages list of messages to delete
	 * @throws AuthenticationException on invalid @param token
	 */
	@PostMapping("delete")
	public void delete(@RequestHeader("Auth-Token") String token, @RequestBody List<MessageDto> deleteMessages) throws AuthenticationException {
		User user = userService.authenticate(token);
		messageService.delete(user, deleteMessages);
	}
	
}
