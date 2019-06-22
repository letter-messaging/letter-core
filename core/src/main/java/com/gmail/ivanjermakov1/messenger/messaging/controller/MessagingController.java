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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
	
	/**
	 * Subscribe on server-sent events.
	 * Calling user will receive events invoked by his own actions.
	 *
	 * @param token user token
	 * @return event aka. {@code com.gmail.ivanjermakov1.messenger.messaging.dto.action.Action} entity
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping("listen")
	public ResponseBodyEmitter getEvents(@RequestParam("token") String token) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		return messagingService.generateRequest(user);
	}
	
	/**
	 * Send message and invoke {@code com.gmail.ivanjermakov1.messenger.messaging.dto.action.MessageEditAction}.
	 *
	 * @param token         user token
	 * @param newMessageDto new message
	 * @return sent message
	 * @throws AuthenticationException on invalid @param token
	 * @throws InvalidMessageException on invalid @param newMessageDto
	 * @throws NoSuchEntityException   on invalid conversationId in @param newMessageDto
	 */
	@PostMapping("send")
	public MessageDto sendMessage(@RequestHeader("Auth-Token") String token,
	                              @RequestBody NewMessageDto newMessageDto) throws AuthenticationException, InvalidMessageException, NoSuchEntityException {
		User user = userService.authenticate(token);
		
		messagingService.processConversationRead(user, newMessageDto.getConversationId());
		return messagingService.processNewMessage(user, newMessageDto);
	}
	
	/**
	 * Edit message.
	 *
	 * @param token          user token
	 * @param editMessageDto editing message
	 * @return edited message
	 * @throws AuthenticationException on invalid @param token
	 * @throws NoSuchEntityException   on invalid conversation
	 */
	@PostMapping("edit")
	public MessageDto editMessage(@RequestHeader("Auth-Token") String token,
	                              @RequestBody EditMessageDto editMessageDto) throws AuthenticationException, NoSuchEntityException {
		User user = userService.authenticate(token);
		
		return messagingService.processMessageEdit(user, editMessageDto);
	}
	
}
