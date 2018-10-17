package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDTO;
import com.gmail.ivanjermakov1.messenger.messaging.service.ConversationService;
import com.gmail.ivanjermakov1.messenger.messaging.service.PreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("preview")
public class PreviewController {
	
	private final UserService userService;
	private final PreviewService previewService;
	private final ConversationService conversationService;
	
	
	@Autowired
	public PreviewController(PreviewService previewService, UserService userService, ConversationService conversationService) {
		this.previewService = previewService;
		this.userService = userService;
		this.conversationService = conversationService;
	}
	
	@GetMapping("all")
	public List<PreviewDTO> all(@RequestParam("token") String token) throws AuthenticationException {
		try {
			User user = userService.getUser(userService.getUserId(token));
			
			return previewService.all(user);
		} catch (NoSuchEntityException e) {
			throw new AuthenticationException("invalid token");
		}
	}
	
	@GetMapping("get")
	public PreviewDTO get(@RequestParam("token") String token,
	                      @RequestParam("conversationId") Long conversationId) throws AuthenticationException {
		try {
			User user = userService.getUser(userService.getUserId(token));
			
			return previewService.getPreview(user, conversationService.getById(conversationId));
		} catch (NoSuchEntityException e) {
			throw new AuthenticationException("invalid token");
		}
	}
	
}
