package com.github.ivanjermakov.lettercore.controller;

import com.github.ivanjermakov.lettercore.dto.ConversationDto;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.mapper.ConversationMapper;
import com.github.ivanjermakov.lettercore.service.ConversationService;
import com.github.ivanjermakov.lettercore.service.UserService;
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
public class ConversationControllerImpl implements ConversationController {

	private final ConversationService conversationService;
	private final UserService userService;

	private ConversationMapper conversationMapper;

	@Autowired
	public ConversationControllerImpl(ConversationService conversationService, UserService userService) {
		this.conversationService = conversationService;
		this.userService = userService;
	}

	@Autowired
	public void setConversationMapper(ConversationMapper conversationMapper) {
		this.conversationMapper = conversationMapper;
	}

	@Override
	@GetMapping("create")
	public ConversationDto create(@ModelAttribute User user, @RequestParam("with") String withLogin) {
		return conversationMapper
				.with(user)
				.map(conversationService.create(user, userService.getUser(withLogin)));
	}

	@Override
	@GetMapping("delete")
	public void delete(@ModelAttribute User user, @RequestParam("id") Long id) throws AuthenticationException {
		conversationService.delete(user, conversationService.get(id));
	}

	@Override
	@GetMapping("hide")
	public void hide(@ModelAttribute User user, @RequestParam("id") Long id) {
		conversationService.hide(user, conversationService.get(id));
	}

}
