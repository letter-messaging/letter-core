package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.service.ConversationService;
import com.gmail.ivanjermakov1.messenger.messaging.service.PreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
	
	/**
	 * List previews.
	 *
	 * @param token user token
	 * @return list of previews
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping("all")
	public List<PreviewDto> all(@RequestHeader("Auth-Token") String token,
	                            Pageable pageable) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		return previewService.all(user, pageable);
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
		User user = userService.authenticate(token);
		Conversation conversation = conversationService.get(conversationId);
		
		PreviewDto preview = previewService.getPreview(user, conversation);
		conversationService.show(user, conversation);
		
		return preview;
	}
	
}
