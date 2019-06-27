package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.core.mapper.ConversationMapper;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewChatDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.ConversationRepository;
import com.gmail.ivanjermakov1.messenger.messaging.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("chat")
@Transactional
public class ChatController {
	
	private final ChatService chatService;
	private final UserService userService;
	private final ConversationController conversationController;
	private final ConversationRepository conversationRepository;
	
	private ConversationMapper conversationMapper;
	
	@Autowired
	public ChatController(UserService userService, ChatService chatService, ConversationController conversationController, ConversationRepository conversationRepository) {
		this.userService = userService;
		this.chatService = chatService;
		this.conversationController = conversationController;
		this.conversationRepository = conversationRepository;
	}
	
	@Autowired
	public void setConversationMapper(ConversationMapper conversationMapper) {
		this.conversationMapper = conversationMapper;
	}
	
	/**
	 * Create chat.
	 *
	 * @param token user token
	 * @param chat  chat instance to create
	 * @return created chat
	 * @throws AuthenticationException on invalid @param token
	 */
	@PostMapping("create")
	public ConversationDto create(@RequestHeader("Auth-Token") String token,
	                              @RequestBody NewChatDto chat) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		return conversationMapper.with(user).map(chatService.create(user, chat));
	}
	
	/**
	 * Add new member to chat.
	 * Every member can add new member. If you need to add multiple new members, use {@code .addMembers()}
	 *
	 * @param token    user token
	 * @param chatId   chat memberId
	 * @param memberId new member id
	 * @throws AuthenticationException on invalid @param token
	 * @throws NoSuchEntityException   if caller is not chat member or no such chat
	 */
	@GetMapping("add")
	public void addMember(@RequestHeader("Auth-Token") String token,
	                      @RequestParam("chatId") Long chatId,
	                      @RequestParam("memberId") Long memberId) throws AuthenticationException, NoSuchEntityException {
		User user = userService.authenticate(token);
		Conversation chat = conversationRepository.findById(chatId)
				.orElseThrow(() -> new NoSuchEntityException("no such chat"));
		User member = userService.getUser(memberId);
		
		chatService.addMembers(user, chat, Collections.singletonList(member));
	}
	
	/**
	 * Add new members to chat.
	 * Every member can add new members.
	 *
	 * @param token     user token
	 * @param chatId    chat id
	 * @param memberIds list of new members ids
	 * @throws AuthenticationException on invalid @param token
	 * @throws NoSuchEntityException   if caller is not chat member
	 */
	@PostMapping("add")
	public void addMembers(@RequestHeader("Auth-Token") String token,
	                       @RequestParam("chatId") Long chatId,
	                       @RequestBody List<Long> memberIds) throws AuthenticationException, NoSuchEntityException {
		User user = userService.authenticate(token);
		
		Conversation chat = conversationRepository.findById(chatId)
				.orElseThrow(() -> new NoSuchEntityException("no such chat"));
		
		List<User> members = memberIds
				.stream()
				.map(userService::getUser)
				.collect(Collectors.toList());
		
		chatService.addMembers(user, chat, members);
	}
	
	/**
	 * Kick member from chat.
	 * Only chat creator can kick members. Creator cannot kick himself, {@code .hide()} method should be used to hide
	 * conversation.
	 *
	 * @param token    user token
	 * @param chatId   chat memberId
	 * @param memberId kick member id
	 * @throws AuthenticationException on invalid @param token
	 * @throws AuthorizationException  if caller is not chat creator
	 * @throws IllegalStateException   if chat creator try to kick himself
	 */
	@GetMapping("kick")
	public void kickMember(@RequestHeader("Auth-Token") String token,
	                       @RequestParam("chatId") Long chatId,
	                       @RequestParam("memberId") Long memberId) throws AuthenticationException, AuthorizationException, IllegalStateException {
		User user = userService.authenticate(token);
		Conversation chat = conversationRepository.findById(chatId)
				.orElseThrow(() -> new NoSuchEntityException("no such chat"));
		User member = userService.getUser(memberId);
		
		chatService.kickMember(user, chat, member);
	}
	
	/**
	 * Hide chat for calling user and delete all messages sent by him.
	 *
	 * @param token          calling user token
	 * @param conversationId id of conversation to delete
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping("delete")
	public void delete(@RequestHeader("Auth-Token") String token,
	                   @RequestParam("id") Long conversationId) throws AuthenticationException {
		conversationController.delete(token, conversationId);
	}
	
	/**
	 * Hide chat from calling user
	 *
	 * @param token          calling user token
	 * @param conversationId id of conversation to hide
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping("hide")
	public void hide(@RequestHeader("Auth-Token") String token,
	                 @RequestParam("id") Long conversationId) throws AuthenticationException {
		conversationController.hide(token, conversationId);
	}
	
}
