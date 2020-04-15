package com.github.ivanjermakov.lettercore.conversation.controller;

import com.github.ivanjermakov.lettercore.auth.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.auth.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.common.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.conversation.dto.ConversationDto;
import com.github.ivanjermakov.lettercore.conversation.dto.NewChatDto;
import com.github.ivanjermakov.lettercore.conversation.entity.Conversation;
import com.github.ivanjermakov.lettercore.conversation.mapper.ConversationMapper;
import com.github.ivanjermakov.lettercore.conversation.repository.ConversationRepository;
import com.github.ivanjermakov.lettercore.conversation.service.ChatService;
import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.user.service.UserService;
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
	 * @param user authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param chat chat instance to create. Creator id is not required.
	 * @return created chat
	 */
	@PostMapping("create")
	public ConversationDto create(@ModelAttribute User user,
	                              @RequestBody NewChatDto chat) {
		return conversationMapper.with(user).map(chatService.create(user, chat));
	}

	/**
	 * Add new member to chat.
	 * Every member can add new member. If you need to add multiple new members, use {@code .addMembers()}
	 *
	 * @param user     authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param chatId   chat memberId
	 * @param memberId new member id
	 * @throws NoSuchEntityException if caller is not chat member or no such chat
	 */
	@GetMapping("add")
	public void addMember(@ModelAttribute User user,
	                      @RequestParam("chatId") Long chatId,
	                      @RequestParam("memberId") Long memberId) throws NoSuchEntityException {
		Conversation chat = conversationRepository.findById(chatId)
				.orElseThrow(() -> new NoSuchEntityException("no such chat"));
		User member = userService.getUser(memberId);

		chatService.addMembers(user, chat, new ArrayList<>(Collections.singletonList(member)));
	}

	/**
	 * Add new members to chat.
	 * Every member can add new members.
	 *
	 * @param user      authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param chatId    chat id
	 * @param memberIds list of new members ids
	 * @throws NoSuchEntityException if caller is not chat member
	 */
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

	/**
	 * Kick member from chat.
	 * Only chat creator can kick members. Creator cannot kick himself, {@code .hide()} method should be used to hide
	 * conversation.
	 *
	 * @param user     authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param chatId   chat memberId
	 * @param memberId kick member id
	 * @throws AuthorizationException if caller is not chat creator
	 * @throws IllegalStateException  if chat creator try to kick himself
	 */
	@GetMapping("kick")
	public void kickMember(@ModelAttribute User user,
	                       @RequestParam("chatId") Long chatId,
	                       @RequestParam("memberId") Long memberId) throws AuthorizationException, IllegalStateException {
		Conversation chat = conversationRepository.findById(chatId)
				.orElseThrow(() -> new NoSuchEntityException("no such chat"));
		User member = userService.getUser(memberId);

		chatService.kickMember(user, chat, member);
	}

	/**
	 * Hide chat for calling user and delete all messages sent by him.
	 *
	 * @param user           authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param conversationId id of conversation to delete
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping("delete")
	public void delete(@ModelAttribute User user,
	                   @RequestParam("id") Long conversationId) throws AuthenticationException {
		conversationController.delete(user, conversationId);
	}

	/**
	 * Hide chat from calling user
	 *
	 * @param user authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param id   id of conversation to hide
	 */
	@GetMapping("hide")
	public void hide(@ModelAttribute User user,
	                 @RequestParam("id") Long id) {
		conversationController.hide(user, id);
	}

}
