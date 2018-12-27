package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.ConversationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ConversationService {
	
	private final static Logger LOG = LoggerFactory.getLogger(ConversationService.class);
	private final ConversationRepository conversationRepository;
	
	@Autowired
	public ConversationService(ConversationRepository conversationRepository) {
		this.conversationRepository = conversationRepository;
	}
	
	public Conversation create(User user, User with) {
		LOG.debug("create conversation between: @" + user.getLogin() + " and @" + with.getLogin());
		
		if (user.getId().equals(with.getId())) return create(user);
		
		try {
			return conversationWith(user, with);
		} catch (NoSuchEntityException e) {
			Conversation conversation = new Conversation(null);
			conversation.setUsers(new ArrayList<>());
			conversation.getUsers().add(user);
			conversation.getUsers().add(with);
			
			conversationRepository.save(conversation);
			
			return conversation;
		}
	}
	
	public Conversation get(Long conversationId) throws NoSuchEntityException {
		return conversationRepository.findById(conversationId).orElseThrow(() -> new NoSuchEntityException("no such conversation"));
	}
	
	public List<Conversation> getConversations(User user) {
		return conversationRepository.getConversations(user.getId());
	}
	
	private Conversation conversationWith(User user1, User user2) throws NoSuchEntityException {
		return conversationRepository.getConversations(user1.getId()).stream()
				.filter(c -> c.getUsers().stream().anyMatch(u -> u.getId().equals(user2.getId())) &&
						c.getUsers().size() == 2)
				.findFirst().orElseThrow(() -> new NoSuchEntityException("no such conversation"));
	}
	
	private Conversation create(User user) {
		Conversation self = conversationRepository.getConversations(user.getId())
				.stream()
				.filter(c -> c.getUsers().size() == 1)
				.findFirst().orElse(null);
		
		if (self != null) return self;
		
		self = new Conversation(null);
		self.setUsers(new ArrayList<>());
		self.getUsers().add(user);
		
		conversationRepository.save(self);
		
		return self;
	}
	
}
