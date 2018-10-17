package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Transactional
public class MessagingService {
	
	private final MessageService messageService;
	private final ConversationService conversationService;
	
	private final Queue<Map.Entry<User, DeferredResult<MessageDTO>>> results = new ConcurrentLinkedQueue<>();
	
	@Autowired
	public MessagingService(MessageService messageService, ConversationService conversationService) {
		this.messageService = messageService;
		this.conversationService = conversationService;
	}
	
	public void addRequest(User user, DeferredResult<MessageDTO> result) {
		results.add(new AbstractMap.SimpleEntry<>(user, result));
	}
	
	public void removeRequest(DeferredResult<MessageDTO> result) {
		results.removeIf(e -> e.getValue() == result);
	}
	
	@Scheduled(fixedDelay = 60000)
	public void clearTimeoutRequests() {
		results.stream()
				.filter(e -> e.getValue().isSetOrExpired())
				.forEach(results::remove);
	}
	
	/**
	 * Processes new message and close requests of /get listeners
	 *
	 * @param user    sender
	 * @param message required fields: message.text, message.conversationId
	 */
	public void process(User user, Message message) {
		message.setSenderId(user.getId());
		message.setSent(Instant.now());
		messageService.save(message);
		
		Conversation conversation = conversationService.getById(message.getConversationId());
		MessageDTO messageDTO = messageService.getFullMessage(message);
		
		results.stream()
				.filter(e -> conversation.getUsers().stream()
						.anyMatch(i -> i.getId().equals(e.getKey().getId())))
				.forEach(e -> {
					e.getValue().setResult(messageDTO);
					removeRequest(e.getValue());
				});
	}
	
}
