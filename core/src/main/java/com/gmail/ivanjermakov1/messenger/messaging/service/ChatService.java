package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.repository.UserRepository;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewChatDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserConversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.ConversationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
				newChat.getName(),
				new ArrayList<>()
		);
		conversationRepository.save(conversation);
		
		conversation.getUserConversations().add(new UserConversation(
				user,
				conversation
		));
		
		if (newChat.getUserIds() != null && !newChat.getUserIds().isEmpty()) {
			conversation.getUserConversations().addAll(
					newChat.getUserIds()
							.stream()
							.filter(id -> !id.equals(user.getId()))
							.map(userRepository::findById)
							.filter(Optional::isPresent)
							.map(Optional::get)
							.map(u -> new UserConversation(u, conversation))
							.collect(Collectors.toList())
			);
		}
		
		Conversation chat = conversationRepository.save(conversation);
		
		try {
			messagingService.systemMessage(NewMessageDto.systemMessage(
					chat.getId(),
					"User @" + user.getLogin() + " created chat"
			));
		} catch (InvalidMessageException ignored) {
		}
		
		return chat;
	}
	
	public void addMembers(User user, Conversation chat, List<User> members) throws AuthorizationException {
		if (chat.getUserConversations().stream().noneMatch(uc -> uc.getUser().getId().equals(user.getId())))
			throw new AuthorizationException("only chat members can add new ones");
		
		chat.getUserConversations().addAll(
				members
						.stream()
						.filter(m -> chat.getUserConversations().stream().noneMatch(member -> member.getUser().getId().equals(m.getId())))
						.map(u -> new UserConversation(u, chat))
						.peek(uc -> {
							try {
								messagingService.systemMessage(NewMessageDto.systemMessage(
										chat.getId(),
										"User @" + user.getLogin() + " added @" + uc.getUser().getLogin() + " to chat"
								));
							} catch (InvalidMessageException ignored) {
							}
						})
						.collect(Collectors.toList())
		);
		
		conversationRepository.save(chat);
	}
	
}
