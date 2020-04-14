package com.gmail.ivanjermakov1.messenger.service;

import com.gmail.ivanjermakov1.messenger.dto.NewChatDto;
import com.gmail.ivanjermakov1.messenger.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.entity.UserConversation;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.repository.ConversationRepository;
import com.gmail.ivanjermakov1.messenger.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

	private final static Logger LOG = LoggerFactory.getLogger(ConversationService.class);
	private final ConversationRepository conversationRepository;
	private final UserRepository userRepository;
	private final MessagingService messagingService;

	@Autowired
	public ChatService(ConversationRepository conversationRepository, UserRepository userRepository, MessagingService messagingService) {
		this.conversationRepository = conversationRepository;
		this.userRepository = userRepository;
		this.messagingService = messagingService;
	}

	public Conversation create(User user, NewChatDto newChat) {
		Conversation conversation = new Conversation(
				newChat.name,
				new ArrayList<>(),
				user
		);
		conversationRepository.save(conversation);

		conversation.userConversations.add(new UserConversation(
				user,
				conversation
		));

		if (newChat.userIds != null && !newChat.userIds.isEmpty()) {
			conversation.userConversations.addAll(
					newChat.userIds
							.stream()
							.filter(id -> !id.equals(user.id))
							.map(userRepository::findById)
							.filter(Optional::isPresent)
							.map(Optional::get)
							.map(u -> new UserConversation(u, conversation))
							.collect(Collectors.toList())
			);
		}

		Conversation chat = conversationRepository.save(conversation);

		messagingService.systemMessage(NewMessageDto.systemMessage(
				chat.id,
				"User @" + user.login + " created chat"
		));

		return chat;
	}

	public void addMembers(User user, Conversation chat, List<User> members) throws NoSuchEntityException {
		if (chat.userConversations.stream().noneMatch(uc -> uc.user.id.equals(user.id)))
			throw new NoSuchEntityException("only chat members can add new ones");

		chat.userConversations.addAll(
				members
						.stream()
						.filter(u -> chat.userConversations
								.stream()
								.noneMatch(uc -> uc.user.id.equals(u.id))
						)
						.map(u -> new UserConversation(u, chat))
						.peek(uc -> {
							messagingService.systemMessage(NewMessageDto.systemMessage(
									chat.id,
									"User @" + user.login + " added @" + uc.user.login + " to chat"
							));
						})
						.collect(Collectors.toList())
		);

		chat.userConversations
				.stream()
				.filter(uc -> members.stream().anyMatch(u -> u.id.equals(uc.user.id)))
				.forEach(uc -> {
					uc.kicked = false;

					messagingService.systemMessage(NewMessageDto.systemMessage(
							chat.id,
							"User @" + user.login + " returned @" + uc.user.login + " to chat"
					));
				});

		conversationRepository.save(chat);
	}

	//	TODO: change kicking logic to support leaving (non-creator users can "kick" themselves)
	public void kickMember(User user, Conversation chat, User member) throws AuthorizationException, IllegalStateException {
		if (!user.id.equals(chat.creator.id))
			throw new AuthorizationException("only chat creator can kick members");
		if (user.id.equals(member.id))
			throw new IllegalStateException("chat creator cannot be kicked");

		UserConversation userConversation = chat.userConversations
				.stream()
				.filter(uc -> uc.user.id.equals(member.id))
				.findFirst()
				.orElseThrow(() -> new NoSuchEntityException("no such member to kick"));

		messagingService.systemMessage(NewMessageDto.systemMessage(
				chat.id,
				"User @" + user.id + " kicked @" + member.id + " from chat"
		));

		userConversation.lastRead = LocalDateTime.now();
		userConversation.kicked = true;

		conversationRepository.save(chat);
	}

}
