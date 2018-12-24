package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.Action;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.ConversationReadAction;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.NewMessageAction;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.Request;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.util.Logs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Transactional
public class MessagingService {
	
	private final static Logger LOG = LoggerFactory.getLogger(UserService.class);
	
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
	@Scheduled(fixedRate = 60000)
	public void clearTimeoutRequests() {
		String before = Logs.collectionSizeList(newMessageRequests, conversationReadRequests);
		
		clearTimeoutRequests(newMessageRequests);
		clearTimeoutRequests(conversationReadRequests);
		
		LOG.info("requests are cleared. \n\t" +
				"before: " + before + "\n\t" +
				"after: " + Logs.collectionSizeList(newMessageRequests, conversationReadRequests));
	}
	
	/**
	 * Processes new message and close requests of /get listeners
	 *
	 * @param user    sender
	 * @param message required fields: message.text, message.conversationId
	 */
	public MessageDTO processNewMessage(User user, Message message) throws NoSuchEntityException {
		LOG.debug("process new message from @" + user.getLogin() + "to conversation @" + message.getConversationId() + "; text: " + message.getText());
		
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
	
	public void processConversationRead(User user, Long conversationId) throws NoSuchEntityException {
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
	
	private void clearTimeoutRequests(Collection<? extends Request<? extends Action>> requests) {
		requests.stream()
				.filter(Request::isSetOrExpired)
				.forEach(requests::remove);
	}
	
}
