package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ConversationService {
	
	private final ConversationRepository conversationRepository;
	
	@Autowired
	public ConversationService(ConversationRepository conversationRepository) {
		this.conversationRepository = conversationRepository;
	}
	
	public Conversation create(User user, User with) {
		Conversation existingConversation = conversationWith(user, with);
		if (existingConversation != null) return existingConversation;
		
		Conversation conversation = new Conversation(null);
		conversation.setUsers(new ArrayList<>());
		conversation.getUsers().add(user);
		conversation.getUsers().add(with);
		
		conversationRepository.save(conversation);
		
		return conversation;
	}
	
	public Conversation getById(Long conversationId) {
		return conversationRepository.findById(conversationId).get();
	}
	
	public List<Conversation> getConversations(User user) {
		return conversationRepository.getConversations(user.getId());
	}
	
	private Conversation conversationWith(User user1, User user2) {
		return conversationRepository.getConversations(user1.getId()).stream()
				.filter(c -> c.getUsers().stream().anyMatch(u -> u.getId().equals(user2.getId())) &&
						c.getUsers().size() == 2)
				.findFirst().orElse(null);
	}
	
}
