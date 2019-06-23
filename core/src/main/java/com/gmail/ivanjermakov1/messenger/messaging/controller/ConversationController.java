package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.core.mapper.ConversationMapper;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.messaging.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("conversation")
@Transactional
public class ConversationController {
	
	private final ConversationService conversationService;
	private final UserService userService;
	
	private ConversationMapper conversationMapper;
	
	@Autowired
	public ConversationController(ConversationService conversationService, UserService userService) {
		this.conversationService = conversationService;
		this.userService = userService;
	}
	
	@Autowired
	public void setConversationMapper(ConversationMapper conversationMapper) {
		this.conversationMapper = conversationMapper;
	}
	
	/**
	 * Create conversation with specified user.
	 * If specified @param withLogin is user himself then "self-conversation" is created.
	 *
	 * @param token     user token
	 * @param withLogin login of user to create conversation with
	 * @return created conversation
	 * @throws AuthenticationException on invalid token
	 */
	@GetMapping("create")
	public ConversationDto create(@RequestHeader("Auth-Token") String token,
	                              @RequestParam("with") String withLogin) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		return conversationMapper
				.with(user)
				.map(conversationService.create(user, userService.getUser(withLogin)));
	}
	
	/**
	 * Hide conversation for calling user and delete all messages sent by him.
	 *
	 * @param token          calling user token
	 * @param conversationId id of conversation to delete
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping("delete")
	public void delete(@RequestHeader("Auth-Token") String token,
	                   @RequestParam("id") Long conversationId) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		conversationService.delete(user, conversationService.get(conversationId));
	}
	
	/**
	 * Hide conversation from calling user
	 *
	 * @param token          calling user token
	 * @param conversationId id of conversation to hide
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping("hide")
	public void hide(@RequestHeader("Auth-Token") String token,
	                 @RequestParam("id") Long conversationId) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		conversationService.hide(user, conversationService.get(conversationId));
	}
	
}
