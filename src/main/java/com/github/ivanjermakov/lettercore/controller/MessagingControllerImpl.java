package com.github.ivanjermakov.lettercore.controller;

import com.github.ivanjermakov.lettercore.dto.EditMessageDto;
import com.github.ivanjermakov.lettercore.dto.MessageDto;
import com.github.ivanjermakov.lettercore.dto.NewMessageDto;
import com.github.ivanjermakov.lettercore.dto.action.Action;
import com.github.ivanjermakov.lettercore.dto.action.Request;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.service.MessagingService;
import com.github.ivanjermakov.lettercore.service.UserService;
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
public class MessagingControllerImpl implements MessagingController {

	private final MessagingService messagingService;
	private final UserService userService;

	@Autowired
	public MessagingControllerImpl(MessagingService messagingService, UserService userService) {
		this.messagingService = messagingService;
		this.userService = userService;
	}

	@Override
	@GetMapping("listen")
	public Flux<Action> getEvents(@RequestParam("token") String token) throws AuthenticationException {
		User user = userService.authenticate(token);

		return Flux.create(sink -> messagingService.connect(new Request<>(user, sink)));
	}

	@Override
	@PostMapping("send")
	@Transactional
	public MessageDto sendMessage(@ModelAttribute User user,
	                              @RequestBody NewMessageDto newMessageDto) throws InvalidEntityException, AuthorizationException {
		messagingService.processConversationRead(user, newMessageDto.conversationId);
		return messagingService.processNewMessage(user, newMessageDto);
	}

	@Override
	@PostMapping("edit")
	@Transactional
	public MessageDto editMessage(@ModelAttribute User user,
	                              @RequestBody EditMessageDto editMessageDto) throws AuthenticationException {
		return messagingService.processMessageEdit(user, editMessageDto);
	}

}
