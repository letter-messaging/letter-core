package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.dto.UserDTO;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService {
	
	private final MessageRepository messageRepository;
	private final UserService userService;
	private final UserMainInfoService userMainInfoService;
	private final ConversationService conversationService;
	
	@Autowired
	public MessageService(MessageRepository messageRepository, UserService userService, UserMainInfoService userMainInfoService, ConversationService conversationService) {
		this.messageRepository = messageRepository;
		this.userService = userService;
		this.userMainInfoService = userMainInfoService;
		this.conversationService = conversationService;
	}
	
	public void save(Message message) {
		messageRepository.save(message);
	}
	
	public Message getLastMessage(Long conversationId) {
		return messageRepository.getTop1ByConversationIdOrderBySentDesc(conversationId);
	}
	
	public List<MessageDTO> get(Long userId, Long conversationId, Integer offset, Integer limit) throws AuthenticationException {
		if (conversationService.getById(conversationId).getUsers().stream().noneMatch(u -> u.getId().equals(userId)))
			throw new AuthenticationException("invalid conversation id");
		
		Set<Message> messagesIds = messageRepository.get(conversationId, offset, limit);
		
		return messagesIds.stream()
				.map(this::getFullMessage)
				.collect(Collectors.toList());
	}
	
	public MessageDTO getFullMessage(Message message) {
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setMessage(message);
		messageDTO.setConversation(new Conversation(message.getConversationId()));
		
		try {
			User user = userService.getUser(message.getSenderId());
			messageDTO.setSender(new UserDTO(user, userMainInfoService.getById(user.getId())));
			userService.full(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return messageDTO;
	}
	
	public void read(User user, Conversation conversation) {
		messageRepository.readAllExcept(user.getId(), conversation.getId());
	}
	
	public Integer unreadCount(User user, Conversation conversation) {
		return messageRepository.getUnreadCount(user.getId(), conversation.getId());
	}
	
}
