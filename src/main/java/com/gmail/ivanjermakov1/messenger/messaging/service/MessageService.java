package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.FullMessage;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.repository.MessageRepository;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserConversationRepository;
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
	private final UserConversationRepository userConversationRepository;
	
	@Autowired
	public MessageService(MessageRepository messageRepository, UserService userService, UserMainInfoService userMainInfoService, UserConversationRepository userConversationRepository) {
		this.messageRepository = messageRepository;
		this.userService = userService;
		this.userMainInfoService = userMainInfoService;
		this.userConversationRepository = userConversationRepository;
	}
	
	public void save(Message message) {
		messageRepository.save(message);
	}
	
	public Message getLastMessage(Long conversationId) {
		return messageRepository.getTop1ByConversationIdOrderBySentDesc(conversationId);
	}
	
	public List<FullMessage> get(Long userId, Long conversationId, Integer offset, Integer limit) throws AuthenticationException {
		if (userConversationRepository.getConversationIds(userId).stream().noneMatch(i -> i.equals(conversationId)))
			throw new AuthenticationException("invalid conversation id");
		
		Set<Message> messagesIds = messageRepository.get(conversationId, offset, limit);
		
		return messagesIds.stream()
				.map(this::getFullMessage)
				.collect(Collectors.toList());
	}
	
	private FullMessage getFullMessage(Message message) {
		FullMessage fullMessage = new FullMessage();
		fullMessage.setMessage(message);
		fullMessage.setConversation(new Conversation(message.getConversationId()));
		
		try {
			User user = userService.getUser(message.getSenderId());
			fullMessage.setSender(user);
			fullMessage.setSenderInfo(userMainInfoService.getById(user.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fullMessage;
	}
	
}
