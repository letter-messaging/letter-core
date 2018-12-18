package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.entity.action.ConversationReadAction;
import com.gmail.ivanjermakov1.messenger.messaging.entity.action.NewMessageAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Transactional
public class MessagingService {
	
	private final MessageService messageService;
	private final ConversationService conversationService;
	private final UserService userService;
	
	private final Queue<Map.Entry<User, DeferredResult<NewMessageAction>>> newMessageRequests = new ConcurrentLinkedQueue<>();
	private final Queue<Map.Entry<User, DeferredResult<ConversationReadAction>>> conversationReadRequests = new ConcurrentLinkedQueue<>();
	
	@Autowired
	public MessagingService(MessageService messageService, ConversationService conversationService, UserService userService) {
		this.messageService = messageService;
		this.conversationService = conversationService;
		this.userService = userService;
	}
	
	public Queue<Map.Entry<User, DeferredResult<NewMessageAction>>> getNewMessageRequests() {
		return newMessageRequests;
	}
	
	public Queue<Map.Entry<User, DeferredResult<ConversationReadAction>>> getConversationReadRequests() {
		return conversationReadRequests;
	}
	
	/**
	 * Clear out timeout requests from all of the request queues
	 */
	@Scheduled(fixedDelay = 60000)
	public void clearTimeoutRequests() {
		newMessageRequests.stream()
				.filter(e -> e.getValue().isSetOrExpired())
				.forEach(newMessageRequests::remove);
		conversationReadRequests.stream()
				.filter(e -> e.getValue().isSetOrExpired())
				.forEach(conversationReadRequests::remove);

//		TODO: deal with generics
//		removeTimeoutRequests(newMessageRequests);
//		removeTimeoutRequests(conversationReadRequests);
	}
	
	/**
	 * Processes new message and close requests of /get listeners
	 *
	 * @param user    sender
	 * @param message required fields: message.text, message.conversationId
	 */
	public MessageDTO processNewMessage(User user, Message message) {
		message.setSenderId(user.getId());
		message.setSent(Instant.now());
		messageService.save(message);
		
		Conversation conversation = conversationService.getById(message.getConversationId());
		MessageDTO messageDTO = messageService.getFullMessage(message);
		
		newMessageRequests.stream()
				.filter(requestEntry -> conversation.getUsers().stream()
						.anyMatch(i -> i.getId().equals(requestEntry.getKey().getId())))
				.forEach(e -> {
					e.getValue().setResult(new NewMessageAction(messageDTO));
					newMessageRequests.removeIf(entry -> entry.getValue() == e.getValue());
				});
		
		return messageService.getFullMessage(message);
	}
	
	public void processConversationRead(User user, Long conversationId) {
		Conversation conversation = conversationService.getById(conversationId);
		
		messageService.read(user, conversation);
		
		conversationReadRequests.stream()
				.filter(requestEntry -> conversation.getUsers().stream()
						.anyMatch(i -> i.getId().equals(requestEntry.getKey().getId())))
				.filter(requestEntry -> !requestEntry.getKey().getId().equals(user.getId()))
				.forEach(e -> {
					e.getValue().setResult(new ConversationReadAction(conversation, userService.full(user)));
					conversationReadRequests.removeIf(entry -> entry.getValue() == e.getValue());
				});
	}
	
	private void removeTimeoutRequests(Queue<Map.Entry<User, DeferredResult<?>>> queue) {
		queue.stream()
				.filter(e -> e.getValue().isSetOrExpired())
				.forEach(queue::remove);
	}
	
}
