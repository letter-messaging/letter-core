package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDto;
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
	
	/**
	 * List all previews.
	 *
	 * @param token user token
	 * @return list of previews
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping("all")
	public List<PreviewDto> all(@RequestHeader("Auth-Token") String token) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		return previewService.all(user);
	}
	
	/**
	 * Get specified conversation.
	 *
	 * @param token          user token
	 * @param conversationId conversation id
	 * @return conversation
	 * @throws AuthenticationException on invalid @param token
	 * @throws NoSuchEntityException   on invalid @param conversationId
	 */
	@GetMapping("get")
	public PreviewDto get(@RequestHeader("Auth-Token") String token,
	                      @RequestParam("conversationId") Long conversationId) throws AuthenticationException, NoSuchEntityException {
		//	TODO: pagination
		User user = userService.authenticate(token);
		
		PreviewDto preview = previewService.getPreview(user, conversationService.get(conversationId));
		messagingService.processConversationRead(user, conversationId);
		
		return preview;
	}
	
}
