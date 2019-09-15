package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.mapper.ConversationMapper;
import com.gmail.ivanjermakov1.messenger.service.ConversationService;
import com.gmail.ivanjermakov1.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	 * @param user      authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param withLogin login of user to create conversation with
	 * @return created conversation
	 */
//	TODO: use withId instead of withLogin to be more consistent
	@GetMapping("create")
	public ConversationDto create(@ModelAttribute User user,
	                              @RequestParam("with") String withLogin) {
		return conversationMapper
				.with(user)
				.map(conversationService.create(user, userService.getUser(withLogin)));
	}

	/**
	 * Hide conversation for calling user and delete all messages sent by him.
	 *
	 * @param user           authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param conversationId id of conversation to delete
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping("delete")
	public void delete(@ModelAttribute User user,
	                   @RequestParam("id") Long conversationId) throws AuthenticationException {
		conversationService.delete(user, conversationService.get(conversationId));
	}

	/**
	 * Hide conversation from calling user
	 *
	 * @param user           authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param conversationId id of conversation to hide
	 */
	@GetMapping("hide")
	public void hide(@ModelAttribute User user,
	                 @RequestParam("id") Long conversationId) {
		conversationService.hide(user, conversationService.get(conversationId));
	}

}
