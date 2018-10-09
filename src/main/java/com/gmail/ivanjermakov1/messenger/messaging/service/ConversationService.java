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
	
	public Long create(User user, User with) {
		Conversation conversation = new Conversation(null);
		conversation.setUsers(new ArrayList<>());
		conversation.getUsers().add(user);
		conversation.getUsers().add(with);
		
		conversationRepository.save(conversation);
		
		return conversation.getId();
	}
	
	public Conversation getById(Long conversationId) {
		return conversationRepository.findById(conversationId).get();
	}
	
	public List<Conversation> getConversations(User user) {
		return conversationRepository.getConversations(user.getId());
	}
	
}
