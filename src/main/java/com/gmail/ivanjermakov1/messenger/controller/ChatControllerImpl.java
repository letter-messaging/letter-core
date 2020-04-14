package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.dto.NewChatDto;
import com.gmail.ivanjermakov1.messenger.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.mapper.ConversationMapper;
import com.gmail.ivanjermakov1.messenger.repository.ConversationRepository;
import com.gmail.ivanjermakov1.messenger.service.ChatService;
import com.gmail.ivanjermakov1.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("chat")
@Transactional
public class ChatControllerImpl implements ChatController {

	private final ChatService chatService;
	private final UserService userService;
	private final ConversationController conversationController;
	private final ConversationRepository conversationRepository;

	private ConversationMapper conversationMapper;

	@Autowired
	public ChatControllerImpl(UserService userService, ChatService chatService, ConversationController conversationController, ConversationRepository conversationRepository) {
		this.userService = userService;
		this.chatService = chatService;
		this.conversationController = conversationController;
		this.conversationRepository = conversationRepository;
	}

	@Autowired
	public void setConversationMapper(ConversationMapper conversationMapper) {
		this.conversationMapper = conversationMapper;
	}

	@Override
	@PostMapping("create")
	public ConversationDto create(@ModelAttribute User user,
	                              @RequestBody NewChatDto chat) {
		return conversationMapper.with(user).map(chatService.create(user, chat));
	}

	@Override
	@GetMapping("add")
	public void addMember(@ModelAttribute User user,
	                      @RequestParam("chatId") Long chatId,
	                      @RequestParam("memberId") Long memberId) throws NoSuchEntityException {
		Conversation chat = conversationRepository.findById(chatId)
				.orElseThrow(() -> new NoSuchEntityException("no such chat"));
		User member = userService.getUser(memberId);

		chatService.addMembers(user, chat, new ArrayList<>(Collections.singletonList(member)));
	}

	@Override
	@PostMapping("add")
	public void addMembers(@ModelAttribute User user,
	                       @RequestParam("chatId") Long chatId,
	                       @RequestBody List<Long> memberIds) throws NoSuchEntityException {
		Conversation chat = conversationRepository.findById(chatId)
				.orElseThrow(() -> new NoSuchEntityException("no such chat"));

		List<User> members = memberIds
				.stream()
				.map(userService::getUser)
				.collect(Collectors.toList());

		chatService.addMembers(user, chat, members);
	}

	@Override
	@GetMapping("kick")
	public void kickMember(@ModelAttribute User user,
	                       @RequestParam("chatId") Long chatId,
	                       @RequestParam("memberId") Long memberId) throws AuthorizationException, IllegalStateException {
		Conversation chat = conversationRepository.findById(chatId)
				.orElseThrow(() -> new NoSuchEntityException("no such chat"));
		User member = userService.getUser(memberId);

		chatService.kickMember(user, chat, member);
	}

	@Override
	@GetMapping("delete")
	public void delete(@ModelAttribute User user,
	                   @RequestParam("id") Long conversationId) throws AuthenticationException {
		conversationController.delete(user, conversationId);
	}

	@Override
	@GetMapping("hide")
	public void hide(@ModelAttribute User user,
	                 @RequestParam("id") Long id) {
		conversationController.hide(user, id);
	}

}
