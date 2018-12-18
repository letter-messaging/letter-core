package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.entity.action.ConversationReadAction;
import com.gmail.ivanjermakov1.messenger.messaging.entity.action.NewMessageAction;
import com.gmail.ivanjermakov1.messenger.messaging.entity.action.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Transactional
public class MessagingService {
	
	private final MessageService messageService;
	private final ConversationService conversationService;
	private final UserService userService;
	
	private final Queue<Request<NewMessageAction>> newMessageRequests = new ConcurrentLinkedQueue<>();
	private final Queue<Request<ConversationReadAction>> conversationReadRequests = new ConcurrentLinkedQueue<>();
	
	@Autowired
	public MessagingService(MessageService messageService, ConversationService conversationService, UserService userService) {
		this.messageService = messageService;
		this.conversationService = conversationService;
		this.userService = userService;
	}
	
	public Queue<Request<NewMessageAction>> getNewMessageRequests() {
		return newMessageRequests;
	}
	
	public Queue<Request<ConversationReadAction>> getConversationReadRequests() {
		return conversationReadRequests;
	}
	
	/**
	 * Clear out timeout requests from all of the request queues
	 */
	@Scheduled(fixedDelay = 60000)
	public void clearTimeoutRequests() {
		newMessageRequests.stream()
				.filter(Request::isSetOrExpired)
				.forEach(newMessageRequests::remove);
		conversationReadRequests.stream()
				.filter(Request::isSetOrExpired)
				.forEach(conversationReadRequests::remove);
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
				.filter(request -> conversation.getUsers()
						.stream()
						.anyMatch(i -> i.getId().equals(request.getUser().getId())))
				.forEach(request -> {
					request.set(new NewMessageAction(messageDTO));
					newMessageRequests.removeIf(r -> r.equals(request));
				});
		
		return messageService.getFullMessage(message);
	}
	
	public void processConversationRead(User user, Long conversationId) {
		Conversation conversation = conversationService.getById(conversationId);
		
		messageService.read(user, conversation);
		
		conversationReadRequests.stream()
				.filter(requestEntry -> conversation.getUsers()
						.stream()
						.anyMatch(i -> i.getId().equals(requestEntry.getUser().getId())))
				.filter(requestEntry -> !requestEntry.getUser().getId().equals(user.getId()))
				.forEach(e -> {
					e.set(new ConversationReadAction(conversation, userService.full(user)));
					conversationReadRequests.removeIf(entry -> entry.equals(e));
				});
	}
	
}
