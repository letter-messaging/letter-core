package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserConversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.ConversationRepository;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@Transactional
public class ConversationService {
	
	private final ConversationRepository conversationRepository;
	private final UserConversationRepository userConversationRepository;
	private final UserService userService;
	
	@Autowired
	public ConversationService(ConversationRepository conversationRepository, UserConversationRepository userConversationRepository, UserService userService) {
		this.conversationRepository = conversationRepository;
		this.userConversationRepository = userConversationRepository;
		this.userService = userService;
	}
	
	public Long create(User user, String withLogin) throws NoSuchEntityException {
		Long conversationId = userConversationRepository.findConversationIdsByUsersIds(
				user.getId(),
				userService.getUser(withLogin).getId()
		);
		
		if (conversationId == null) {
			Conversation conversation = new Conversation(null);
			conversationRepository.save(conversation);
			
			userConversationRepository.save(new UserConversation(user.getId(), conversation.getId()));
			userConversationRepository.save(new UserConversation(userService.getUser(withLogin).getId(), conversation.getId()));
			
			return conversation.getId();
		} else {
			return conversationId;
		}
		
	}
	
	public Set<Long> getUsersIds(Long conversationId) {
		return userConversationRepository.getUsersIds(conversationId);
	}
	
	public Set<Long> allIds(User user) {
		return userConversationRepository.getConversationIds(user.getId());
	}
	
}
