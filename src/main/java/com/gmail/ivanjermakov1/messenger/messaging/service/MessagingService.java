package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Transactional
public class MessagingService {
	
	private final MessageService messageService;
	private final ConversationService conversationService;
	
	private final Queue<Map.Entry<User, DeferredResult<Message>>> results = new ConcurrentLinkedQueue<>();
	
	@Autowired
	public MessagingService(MessageService messageService, ConversationService conversationService) {
		this.messageService = messageService;
		this.conversationService = conversationService;
	}
	
	public void addRequest(User user, DeferredResult<Message> result) {
		results.add(new AbstractMap.SimpleEntry<>(user, result));
	}
	
	public void removeRequest(DeferredResult<Message> result) {
		results.removeIf(e -> e.getValue() == result);
	}
	
	public void process(User user, Message message) {
		message.setSenderId(user.getId());
		message.setSent(Instant.now());
		messageService.save(message);
		
		Set<Long> usersIds = conversationService.getUsersIds(message.getConversationId());
		
		results.stream()
				.filter(e -> usersIds.stream()
						.anyMatch(i -> i.equals(e.getKey().getId())))
				.forEach(e -> {
					e.getValue().setResult(message);
					removeRequest(e.getValue());
				});
	}
	
}
