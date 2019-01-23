package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDTO;
import com.gmail.ivanjermakov1.messenger.messaging.service.ConversationService;
import com.gmail.ivanjermakov1.messenger.messaging.service.MessagingService;
import com.gmail.ivanjermakov1.messenger.messaging.service.PreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("preview")
public class PreviewController {
	
	private final UserService userService;
	private final PreviewService previewService;
	private final ConversationService conversationService;
	private final MessagingService messagingService;
	
	@Autowired
	public PreviewController(PreviewService previewService, UserService userService, ConversationService conversationService, MessagingService messagingService) {
		this.previewService = previewService;
		this.userService = userService;
		this.conversationService = conversationService;
		this.messagingService = messagingService;
	}
	
	@GetMapping("all")
	public List<PreviewDTO> all(@RequestHeader("Auth-Token") String token) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		return previewService.all(user);
	}
	
	@GetMapping("get")
	public PreviewDTO get(@RequestHeader("Auth-Token") String token,
	                      @RequestParam("conversationId") Long conversationId) throws AuthenticationException, NoSuchEntityException {
		User user = userService.authenticate(token);
		
		PreviewDTO preview = previewService.getPreview(user, conversationService.get(conversationId));
		messagingService.processConversationRead(user, conversationId);
		
		return preview;
	}
	
}
