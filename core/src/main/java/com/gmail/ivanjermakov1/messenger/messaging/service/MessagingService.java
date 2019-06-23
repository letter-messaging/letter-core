package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidMessageException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.EditMessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.Action;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.ConversationReadAction;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.MessageEditAction;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.NewMessageAction;
import com.gmail.ivanjermakov1.messenger.messaging.dto.action.Request;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Document;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Image;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserConversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserConversationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessagingService {
	
	private final static Logger LOG = LoggerFactory.getLogger(MessagingService.class);
	
	private final MessageService messageService;
	private final ConversationService conversationService;
	private final UserService userService;
	private final ImageService imageService;
	private final DocumentService documentService;
	private final UserConversationRepository userConversationRepository;
	
	private final List<Request<Action>> requests = new CopyOnWriteArrayList<>();
	
	@Value("${sse.timeout}")
	private Long sseTimeout;
	
	@Autowired
	public MessagingService(MessageService messageService, ConversationService conversationService, UserService userService, ImageService imageService, DocumentService documentService, UserConversationRepository userConversationRepository) {
		this.messageService = messageService;
		this.conversationService = conversationService;
		this.userService = userService;
		this.imageService = imageService;
		this.documentService = documentService;
		this.userConversationRepository = userConversationRepository;
	}
	
	public SseEmitter generateRequest(User user) {
		SseEmitter emitter = new SseEmitter(sseTimeout);
		Request<Action> request = new Request<>(user, emitter);
		emitter.onTimeout(() -> requests.remove(request));
		requests.add(request);
		return emitter;
	}
	
	public void sendResponse(Request<Action> request, Action action) {
		LOG.debug("sending response to @" + request.getUser().getLogin() + "; type: " + action.getType());
		try {
			SseEmitter.SseEventBuilder event = SseEmitter.event()
					.data(action)
					.reconnectTime(100);
			request.getEmitter().send(event);
		} catch (IOException e) {
			requests.remove(request);
		}
	}
	
	public void forEachRequest(Conversation conversation, Consumer<Request<Action>> consumer) {
		new Thread(() -> requests
				.stream()
				.filter(r -> conversation.getUsers()
						.stream()
						.anyMatch(i -> i.getId().equals(r.getUser().getId())))
				.forEach(consumer)).run();
	}
	
	public MessageDto processNewMessage(User user, NewMessageDto newMessageDto) throws InvalidMessageException {
		LOG.debug("process new message from @" + user.getLogin() + " to conversation @" + newMessageDto.getConversationId() +
				"; text: " + newMessageDto.getText());
		
		Conversation conversation = conversationService.get(newMessageDto.getConversationId());
		
		Message message = new Message(
				conversation,
				LocalDateTime.now(),
				newMessageDto.getText(),
				user,
				newMessageDto.getForwarded()
						.stream()
						.map(dto -> messageService.get(dto.getId()))
						.collect(Collectors.toList()),
				newMessageDto.getImages()
						.stream()
						.map(i -> new Image(user, i.getPath(), LocalDate.now()))
						.collect(Collectors.toList()),
				newMessageDto.getDocuments()
						.stream()
						.map(d -> new Document(user, d.getPath(), LocalDate.now()))
						.collect(Collectors.toList())
		);
		
		if (!message.validate()) throw new InvalidMessageException("invalid message");
		
		message = messageService.save(message);
		
		UserConversation userConversation = userConversationRepository.findByUserAndConversation(user, conversation)
				.orElseThrow(NoSuchEntityException::new);
		userConversationRepository.save(userConversation);
		
		MessageDto messageDto = messageService.getFullMessage(user, message);
		
		forEachRequest(conversation, request -> sendResponse(request, new NewMessageAction(messageDto)));
		
		return messageDto;
	}
	
	public MessageDto processMessageEdit(User user, EditMessageDto editMessageDto) throws AuthenticationException {
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
		
		original.getDocuments()
				.stream()
				.filter(d -> editMessageDto.getDocuments()
						.stream()
						.noneMatch(ed -> ed.getId().equals(d.getId())))
				.forEach(documentService::delete);
		
		original = messageService.save(original);
		
		Conversation conversation = conversationService.get(original.getConversation().getId());
		MessageDto messageDto = messageService.getFullMessage(user, original);
		
		forEachRequest(conversation, request -> sendResponse(request, new MessageEditAction(messageDto)));
		
		return messageDto;
	}
	
	public void processConversationRead(User user, Long conversationId) {
		Conversation conversation = conversationService.get(conversationId);
		
		messageService.read(user, conversation);
		
		requests
				.stream()
				.filter(requestEntry -> conversation.getUsers()
						.stream()
						.anyMatch(i -> i.getId().equals(requestEntry.getUser().getId())))
				.forEach(request -> sendResponse(
						request,
						new ConversationReadAction(conversationService.get(user, conversation), userService.full(user)))
				);
		
		forEachRequest(conversation, request -> sendResponse(
				request,
				new ConversationReadAction(conversationService.get(user, conversation), userService.full(user))
		));
	}
	
}
