package com.github.ivanjermakov.lettercore.messaging.controller;

import com.github.ivanjermakov.lettercore.auth.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.auth.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.common.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.messaging.dto.EditMessageDto;
import com.github.ivanjermakov.lettercore.messaging.dto.MessageDto;
import com.github.ivanjermakov.lettercore.messaging.dto.NewMessageDto;
import com.github.ivanjermakov.lettercore.messaging.dto.action.Action;
import com.github.ivanjermakov.lettercore.messaging.dto.action.Request;
import com.github.ivanjermakov.lettercore.messaging.service.MessagingService;
import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
	 * @return event stream of {@link Action}s
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping("listen")
	public Flux<Action> getEvents(@RequestParam("token") String token) throws AuthenticationException {
		User user = userService.authenticate(token);

		return Flux.create(sink -> messagingService.connect(new Request<>(user, sink)));
	}

	/**
	 * Send message and invoke {@code com.gmail.ivanjermakov1.messenger.dto.action.MessageEditAction}.
	 *
	 * @param user          authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param newMessageDto new message
	 * @return sent message
	 * @throws InvalidEntityException on invalid @param newMessageDto
	 */
	@PostMapping("send")
	@Transactional
	public MessageDto sendMessage(@ModelAttribute User user,
	                              @RequestBody NewMessageDto newMessageDto) throws InvalidEntityException, AuthorizationException {
		messagingService.processConversationRead(user, newMessageDto.conversationId);
		return messagingService.processNewMessage(user, newMessageDto);
	}

	/**
	 * Edit message.
	 *
	 * @param user           authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param editMessageDto editing message
	 * @return edited message
	 * @throws AuthenticationException on invalid @param token
	 */
	@PostMapping("edit")
	@Transactional
	public MessageDto editMessage(@ModelAttribute User user,
	                              @RequestBody EditMessageDto editMessageDto) throws AuthenticationException {
		return messagingService.processMessageEdit(user, editMessageDto);
	}

}
