package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.EditMessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.*;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Image;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessagingService {
	
	private final static Logger LOG = LoggerFactory.getLogger(MessagingService.class);
	
	private final MessageService messageService;
	private final ConversationService conversationService;
	private final UserService userService;
	private final ImageService imageService;
	
	private final List<Request<Action>> requests = new CopyOnWriteArrayList<>();
	
	@Autowired
	public MessagingService(MessageService messageService, ConversationService conversationService, UserService userService, ImageService imageService) {
		this.messageService = messageService;
		this.conversationService = conversationService;
		this.userService = userService;
		this.imageService = imageService;
	}
	
	public SseEmitter generateRequest(User user) {
		SseEmitter emitter = new SseEmitter();
		Request<Action> request = new Request<>(user, emitter);
		emitter.onTimeout(() -> {
			emitter.complete();
			requests.remove(request);
		});
		requests.add(request);
		return emitter;
	}
	
	public void sendRequest(Request<Action> request, Action action) {
		LOG.debug("sending request from @" + request.getUser().getLogin());
		try {
			request.getEmitter().send(action);
		} catch (IOException e) {
			LOG.warn("failed to send request from @" + request.getUser().getLogin());
			request.getEmitter().complete();
		}
	}
	
	public MessageDto processNewMessage(User user, NewMessageDto newMessageDto) throws NoSuchEntityException, InvalidMessageException {
		LOG.debug("process new message from @" + user.getLogin() + " to conversation @" + newMessageDto.getConversationId() +
				"; text: " + newMessageDto.getText());
		
		Conversation conversation = conversationService.get(newMessageDto.getConversationId());
		
		Message message = new Message(
				conversation,
				LocalDateTime.now(),
				newMessageDto.getText(),
				conversation.getUsers().size() == 1,
				user,
				newMessageDto.getForwarded()
						.stream()
						.map(dto -> messageService.get(dto.getId()))
						.collect(Collectors.toList()),
				newMessageDto.getImages()
						.stream()
						.map(i -> new Image(user, i.getPath(), LocalDate.now()))
						.collect(Collectors.toList())
		);
		
		if (!message.validate()) throw new InvalidMessageException("invalid message");
		
		message = messageService.save(message);
		
		MessageDto messageDto = messageService.getFullMessage(message);
		
		requests
				.stream()
				.filter(request -> conversation.getUsers()
						.stream()
						.anyMatch(u -> u.getId().equals(request.getUser().getId())))
				.forEach(request -> sendRequest(request, new NewMessageAction(messageDto)));
		
		return messageDto;
	}
	
	public MessageDto processMessageEdit(User user, EditMessageDto editMessageDto) throws NoSuchEntityException, AuthenticationException {
		LOG.debug("process message edit @" + editMessageDto.getId() + "; text: " + editMessageDto.getText());
		
		Message original = messageService.get(editMessageDto.getId());
		
		if (original == null || !original.getSender().getId().equals(user.getId()))
			throw new AuthenticationException("user can edit only own messages");
		
		original.setText(editMessageDto.getText());
		if (editMessageDto.getForwarded() != null && editMessageDto.getForwarded().isEmpty())
			messageService.deleteForwarded(original);
		
		original.getImages()
				.stream()
				.filter(i -> editMessageDto.getImages()
						.stream()
						.noneMatch(ei -> ei.getId().equals(i.getId())))
				.forEach(imageService::delete);
		
		original = messageService.save(original);
		
		Conversation conversation = conversationService.get(original.getConversation().getId());
		MessageDto messageDto = messageService.getFullMessage(original);
		
		requests
				.stream()
				.filter(request -> conversation.getUsers()
						.stream()
						.anyMatch(u -> u.getId().equals(request.getUser().getId())))
				.forEach(request -> sendRequest(request, new MessageEditAction(messageDto)));
		
		return messageDto;
	}
	
	public void processConversationRead(User user, Long conversationId) throws NoSuchEntityException {
		Conversation conversation = conversationService.get(conversationId);
		
		messageService.read(user, conversation);
		
		requests
				.stream()
				.filter(requestEntry -> conversation.getUsers()
						.stream()
						.anyMatch(i -> i.getId().equals(requestEntry.getUser().getId())))
				.forEach(request -> sendRequest(
						request,
						new ConversationReadAction(conversationService.get(user, conversation), userService.full(user)))
				);
	}
	
}
