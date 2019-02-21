package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.messaging.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("conversation")
public class ConversationController {
	
	private final ConversationService conversationService;
	private final UserService userService;
	
	@Autowired
	public ConversationController(ConversationService conversationService, UserService userService) {
		this.conversationService = conversationService;
		this.userService = userService;
	}
	
	/**
	 * Create conversation with specified user.
	 * If specified @param withLogin is user himself then "self-conversation" is created.
	 *
	 * @param token     user token
	 * @param withLogin login of user to create conversation with
	 * @return created conversation
	 * @throws AuthenticationException on invalid token
	 * @throws NoSuchEntityException   on invalid @param withLogin
	 */
	@GetMapping("create")
	public ConversationDto create(@RequestHeader("Auth-Token") String token, @RequestParam("with") String withLogin) throws AuthenticationException, NoSuchEntityException {
		User user = userService.authenticate(token);
		try {
			return conversationService.get(user, conversationService.create(user, userService.getUser(withLogin)));
		} catch (NoSuchEntityException e) {
			throw new NoSuchEntityException("no such user to create conversation with");
		}
	}
	
	/**
	 * Hide conversation for calling user and delete all messages sent by him.
	 *
	 * @param token          calling user token
	 * @param conversationId id of conversation to delete
	 * @throws AuthenticationException on invalid @param token
	 * @throws NoSuchEntityException   on invalid @param conversationId
	 */
	@GetMapping("delete")
	public void delete(@RequestHeader("Auth-Token") String token, @RequestParam("id") Long conversationId) throws AuthenticationException, NoSuchEntityException {
		User user = userService.authenticate(token);
		
		conversationService.delete(user, conversationService.get(conversationId));
	}
	
	/**
	 * Hide conversation from calling user
	 *
	 * @param token          calling user token
	 * @param conversationId id of conversation to hide
	 * @throws AuthenticationException on invalid @param token
	 * @throws NoSuchEntityException   on invalid @param conversationId
	 */
	@GetMapping("hide")
	public void hide(@RequestHeader("Auth-Token") String token, @RequestParam("id") Long conversationId) throws AuthenticationException, NoSuchEntityException {
		User user = userService.authenticate(token);
		
		conversationService.hide(user, conversationService.get(conversationId));
	}
	
}
