package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserConversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.ConversationRepository;
import com.gmail.ivanjermakov1.messenger.messaging.repository.MessageRepository;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserConversationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationService {

	private final static Logger LOG = LoggerFactory.getLogger(ConversationService.class);
	private final ConversationRepository conversationRepository;
	private final MessageRepository messageRepository;
	private final UserConversationRepository userConversationRepository;
	private MessageService messageService;

	@Autowired
	public ConversationService(ConversationRepository conversationRepository, MessageRepository messageRepository, UserConversationRepository userConversationRepository) {
		this.conversationRepository = conversationRepository;
		this.messageRepository = messageRepository;
		this.userConversationRepository = userConversationRepository;
	}

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public Conversation create(User user, User with) {
		LOG.debug("create conversation between: @" + user.login + " and @" + with.login);

		if (user.id.equals(with.id)) return create(user);

		Conversation conversation = new Conversation();
		try {
			conversation = conversationWith(user, with);
			show(user, conversation);
		} catch (NoSuchEntityException e) {
			conversation = conversationRepository.save(conversation);

			conversation.userConversations = new ArrayList<>();
			conversation.userConversations.add(new UserConversation(user, conversation));
			conversation.userConversations.add(new UserConversation(with, conversation));
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
		if (conversation.userConversations.stream().noneMatch(uc -> uc.user.id.equals(user.id)))
			throw new AuthenticationException("you can delete only conversations you are member within");

		messageService.deleteAll(user, conversation);
		hide(user, conversation);
	}

	public Conversation get(Long conversationId) {
		return conversationRepository.findById(conversationId)
				.orElseThrow(() -> new NoSuchEntityException("no such conversation"));
	}

	public List<Conversation> getConversations(User user, Pageable pageable) {
		return conversationRepository.getConversations(user.id, pageable);
	}

	public void hide(User user, Conversation conversation) {
		setHidden(user, conversation, true);
	}

	public void show(User user, Conversation conversation) {
		setHidden(user, conversation, false);
	}

	public void setHidden(User user, Conversation conversation, boolean hidden) {
		UserConversation userConversation = userConversationRepository.findByUserAndConversation(user, conversation)
				.orElseThrow(NoSuchEntityException::new);

		userConversation.hidden = hidden;
		userConversationRepository.save(userConversation);
	}

	private Conversation conversationWith(User user1, User user2) {
		return conversationRepository.getConversations(user1.id, PageRequest.of(0, Integer.MAX_VALUE))
				.stream()
				.filter(c -> c.userConversations
						.stream()
						.map(uc -> uc.user)
						.anyMatch(u -> u.id.equals(user2.id)) &&
						c.userConversations.size() == 2)
				.findFirst()
				.orElseThrow(() -> new NoSuchEntityException("no such conversation"));
	}

	private Conversation create(User user) {
		Conversation self = conversationRepository.getConversations(user.id, PageRequest.of(0, Integer.MAX_VALUE))
				.stream()
				.filter(c -> c.userConversations.size() == 1)
				.findFirst().orElse(null);

		if (self != null) return self;

		self = new Conversation();
		self.userConversations = new ArrayList<>();

		self = conversationRepository.save(self);

		self.userConversations.add(new UserConversation(user, self));

		return conversationRepository.save(self);
	}

	public Integer unreadCount(User user, Conversation conversation) {
		return messageRepository.countUnread(
				user,
				conversation,
				userConversationRepository.findByUserAndConversation(user, conversation)
						.orElseThrow(NoSuchEntityException::new).lastRead
		);
	}

}
