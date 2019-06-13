package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.ConversationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConversationService {
	
	private final static Logger LOG = LoggerFactory.getLogger(ConversationService.class);
	private final ConversationRepository conversationRepository;
	private final UserService userService;
	private MessageService messageService;
	
	@Autowired
	public ConversationService(ConversationRepository conversationRepository, UserService userService) {
		this.conversationRepository = conversationRepository;
		this.userService = userService;
	}
	
	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	
	public Conversation create(User user, User with) {
		LOG.debug("create conversation between: @" + user.getLogin() + " and @" + with.getLogin());
		
		if (user.getId().equals(with.getId())) return create(user);
		
		Conversation conversation = new Conversation();
		try {
			conversation = conversationWith(user, with);
			show(user, conversation);
		} catch (NoSuchEntityException e) {
			conversation.setUsers(new ArrayList<>());
			conversation.getUsers().add(user);
			conversation.getUsers().add(with);
		}
		
		return conversationRepository.save(conversation);
	}
	
	/**
	 * Deleting conversation means deleting all self-sent messages of interrogator within that conversation. It also
	 * affects all source messages infected in forwarding and media within.
	 *
	 * @param user         deletion interrogator
	 * @param conversation conversation to delete
	 * @throws AuthenticationException when interrogator has no permission to delete defined conversation
	 */
	public void delete(User user, Conversation conversation) throws AuthenticationException {
		if (conversation.getUsers().stream().noneMatch(u -> u.getId().equals(user.getId())))
			throw new AuthenticationException("you can delete only conversations you are member within");
		
		messageService.deleteAll(user, conversation);
		hide(user, conversation);
	}
	
	public Conversation get(Long conversationId) throws NoSuchEntityException {
		return conversationRepository.findById(conversationId).orElseThrow(() -> new NoSuchEntityException("no such conversation"));
	}
	
	public List<Conversation> getConversations(User user) {
		return conversationRepository.getConversations(user.getId());
	}
	
	public ConversationDto get(User user, Conversation conversation) {
		return new ConversationDto(
				conversation.getId(),
				isHidden(user, conversation),
				conversation.getUsers()
						.stream()
						.map(userService::full)
						.collect(Collectors.toList())
		);
	}
	
	public void hide(User user, Conversation conversation) {
		conversationRepository.hide(user.getId(), conversation.getId());
	}
	
	public void show(User user, Conversation conversation) {
		conversationRepository.show(user.getId(), conversation.getId());
	}
	
	public Boolean isHidden(User user, Conversation conversation) {
		return conversationRepository.isHidden(user.getId(), conversation.getId());
	}
	
	private Conversation conversationWith(User user1, User user2) throws NoSuchEntityException {
		return conversationRepository.getConversations(user1.getId())
				.stream()
				.filter(c -> c.getUsers()
						.stream()
						.anyMatch(u -> u.getId().equals(user2.getId())) &&
						c.getUsers().size() == 2)
				.findFirst().orElseThrow(() -> new NoSuchEntityException("no such conversation"));
	}
	
	private Conversation create(User user) {
		Conversation self = conversationRepository.getConversations(user.getId())
				.stream()
				.filter(c -> c.getUsers().size() == 1)
				.findFirst().orElse(null);
		
		if (self != null) return self;
		
		self = new Conversation();
		self.setUsers(new ArrayList<>());
		self.getUsers().add(user);
		
		return conversationRepository.save(self);
	}
	
}
