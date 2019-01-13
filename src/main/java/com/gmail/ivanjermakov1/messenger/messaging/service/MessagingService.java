package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.EditMessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewMessageDTO;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.ConversationReadAction;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.MessageEditAction;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.NewMessageAction;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.Request;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessagingService {
	
	private final static Logger LOG = LoggerFactory.getLogger(MessagingService.class);
	
	private final MessageService messageService;
	private final ConversationService conversationService;
	private final UserService userService;
	
	private final Queue<Request<NewMessageAction>> newMessageRequests = new ConcurrentLinkedQueue<>();
	private final Queue<Request<MessageEditAction>> messageEditRequests = new ConcurrentLinkedQueue<>();
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
	
	public Queue<Request<MessageEditAction>> getMessageEditRequests() {
		return messageEditRequests;
	}
	
	public Queue<Request<ConversationReadAction>> getConversationReadRequests() {
		return conversationReadRequests;
	}
	
	/**
	 * Processes new message and close requests of /get listeners
	 *
	 * @param user          sender
	 * @param newMessageDTO new message
	 */
	public MessageDTO processNewMessage(User user, NewMessageDTO newMessageDTO) throws NoSuchEntityException, InvalidMessageException {
		LOG.debug("process new message from @" + user.getLogin() + " to conversation @" + newMessageDTO.getConversationId() +
				"; text: " + newMessageDTO.getText());
		
		Conversation conversation = conversationService.get(newMessageDTO.getConversationId());
		
		Message message = new Message(
				conversation,
				LocalDateTime.now(),
				newMessageDTO.getText(),
				false,
				user,
				newMessageDTO.getForwarded()
						.stream()
						.map(dto -> messageService.get(dto.getId()))
						.collect(Collectors.toList())
		);
		
		if (!message.validate()) throw new InvalidMessageException("invalid message");
		
		if (conversation.getUsers().size() == 1) {
			message.setRead(true);
		}
		
		message = messageService.save(message);
		
		MessageDTO messageDTO = messageService.getFullMessage(message);
		
		newMessageRequests
				.stream()
				.filter(request -> conversation.getUsers()
						.stream()
						.anyMatch(i -> i.getId().equals(request.getUser().getId())))
				.forEach(request -> {
					request.set(new NewMessageAction(messageDTO));
					newMessageRequests.removeIf(r -> r.equals(request));
				});
		
		return messageDTO;
	}
	
	public MessageDTO processMessageEdit(User user, EditMessageDTO editMessageDTO) throws NoSuchEntityException, AuthenticationException {
		LOG.debug("process message edit @" + editMessageDTO.getId() + "; text: " + editMessageDTO.getText());
		
		Message original = messageService.get(editMessageDTO.getId());
		
		if (original == null || !original.getSender().getId().equals(user.getId()))
			throw new AuthenticationException("user can edit only own messages");
		
		original.setText(editMessageDTO.getText());
		if (editMessageDTO.getForwarded() != null && editMessageDTO.getForwarded().isEmpty())
			messageService.deleteForwarded(original);
		original = messageService.save(original);
		
		Conversation conversation = conversationService.get(original.getConversation().getId());
		
		MessageDTO messageDTO = messageService.getFullMessage(original);
		
		messageEditRequests
				.stream()
				.filter(request -> conversation.getUsers()
						.stream()
						.anyMatch(i -> i.getId().equals(request.getUser().getId())))
				.forEach(request -> {
					request.set(new MessageEditAction(messageDTO));
					messageEditRequests.removeIf(r -> r.equals(request));
				});
		
		return messageDTO;
	}
	
	public void processConversationRead(User user, Long conversationId) throws NoSuchEntityException {
		Conversation conversation = conversationService.get(conversationId);
		
		messageService.read(user, conversation);
		
		conversationReadRequests
				.stream()
				.filter(requestEntry -> conversation.getUsers()
						.stream()
						.anyMatch(i -> i.getId().equals(requestEntry.getUser().getId())))
				.forEach(e -> {
					e.set(new ConversationReadAction(conversationService.get(conversation), userService.full(user)));
					conversationReadRequests.removeIf(entry -> entry.equals(e));
				});
	}
	
}
