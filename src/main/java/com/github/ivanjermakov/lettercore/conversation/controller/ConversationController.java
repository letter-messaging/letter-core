package com.github.ivanjermakov.lettercore.conversation.controller;

import com.github.ivanjermakov.lettercore.auth.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.conversation.dto.ConversationDto;
import com.github.ivanjermakov.lettercore.conversation.mapper.ConversationMapper;
import com.github.ivanjermakov.lettercore.conversation.service.ConversationService;
import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.user.service.UserService;
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
	@GetMapping("create")
	public ConversationDto create(@ModelAttribute User user, @RequestParam("with") String withLogin) {
		return conversationMapper
				.with(user)
				.map(conversationService.create(user, userService.getUser(withLogin)));
	}

	/**
	 * Hide conversation for calling user and delete all messages sent by him.
	 *
	 * @param user authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param id   id of conversation to delete
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping("delete")
	public void delete(@ModelAttribute User user, @RequestParam("id") Long id) throws AuthenticationException {
		conversationService.delete(user, conversationService.get(id));
	}

	/**
	 * Hide conversation from calling user
	 *
	 * @param user authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param id   id of conversation to hide
	 */
	@GetMapping("hide")
	public void hide(@ModelAttribute User user, @RequestParam("id") Long id) {
		conversationService.hide(user, conversationService.get(id));
	}

}
