package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService {
	
	private final MessageRepository messageRepository;
	private final UserService userService;
	private ConversationService conversationService;
	
	@Autowired
	public MessageService(MessageRepository messageRepository, UserService userService) {
		this.messageRepository = messageRepository;
		this.userService = userService;
	}
	
	@Autowired
	public void setConversationService(ConversationService conversationService) {
		this.conversationService = conversationService;
	}
	
	public Message save(Message message) {
		return messageRepository.save(message);
	}
	
	public Message getLastMessage(Long conversationId) {
		return messageRepository.getTop1ByConversationIdOrderBySentDesc(conversationId);
	}
	
	public List<MessageDTO> get(Long userId, Long conversationId, Integer offset, Integer limit) throws AuthenticationException, NoSuchEntityException {
		if (conversationService.get(conversationId).getUsers().stream().noneMatch(u -> u.getId().equals(userId)))
			throw new AuthenticationException("invalid conversation id");
		
		Set<Message> messagesIds = messageRepository.get(conversationId, offset, limit);
		
		return messagesIds
				.stream()
				.map(this::getFullMessage)
				.collect(Collectors.toList());
	}
	
	public MessageDTO getFullMessage(Message message) {
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setId(message.getId());
		messageDTO.setSent(message.getSent());
		messageDTO.setRead(message.getRead());
		messageDTO.setText(message.getText());
		
		try {
			Conversation conversation = conversationService.get(message.getConversation().getId());
			messageDTO.setConversation(conversationService.get(message.getSender(), conversation));
		} catch (NoSuchEntityException e) {
			e.printStackTrace();
		}
		
		try {
			User user = userService.getUser(message.getSender().getId());
			messageDTO.setSender(userService.full(user));
			userService.full(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		messageDTO.setForwarded(Optional
				.ofNullable(messageRepository.getById(message.getId()).getForwarded())
				.orElse(Collections.emptyList())
				.stream()
				.map(this::getFullMessage)
				.collect(Collectors.toList()));
		
		return messageDTO;
	}
	
	public void read(User user, Conversation conversation) {
		messageRepository.readAllExcept(user.getId(), conversation.getId());
	}
	
	public Integer unreadCount(User user, Conversation conversation) {
		return messageRepository.getUnreadCount(user.getId(), conversation.getId());
	}
	
	/**
	 * Allowed to delete only user-self messages
	 *
	 * @param user           messages owner
	 * @param deleteMessages messages which going to be removed
	 */
	public void delete(User user, List<MessageDTO> deleteMessages) {
		deleteMessages
				.stream()
				.map(dto -> messageRepository.getById(dto.getId()))
				.filter(m -> m.getSender().getId().equals(user.getId()))
				.forEach(this::delete);
	}
	
	public void deleteForwarded(Message message) {
		messageRepository.deleteForwarded(message.getId());
	}
	
	public Message get(Long messageId) {
		return messageRepository.getById(messageId);
	}
	
	public void deleteAll(User user, Conversation conversation) {
//		TODO: optimize
		messageRepository.getAllBySenderAndConversation(user, conversation).stream().parallel().forEach(this::delete);
	}
	
	/**
	 * Delete messages different way then just <code>.delete()</code>. Firstly deleting current message from all over
	 * forwarded messages, then deletes itself
	 *
	 * @param message message that will be deleted
	 */
	public void delete(Message message) {
		messageRepository.deleteFromForwarded(message.getId());
		messageRepository.delete(message);
	}
	
}
