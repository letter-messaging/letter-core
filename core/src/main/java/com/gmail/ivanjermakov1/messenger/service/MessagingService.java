package com.gmail.ivanjermakov1.messenger.service;

import com.gmail.ivanjermakov1.messenger.dto.EditMessageDto;
import com.gmail.ivanjermakov1.messenger.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.dto.action.Action;
import com.gmail.ivanjermakov1.messenger.dto.action.ConversationReadAction;
import com.gmail.ivanjermakov1.messenger.dto.action.MessageEditAction;
import com.gmail.ivanjermakov1.messenger.dto.action.NewMessageAction;
import com.gmail.ivanjermakov1.messenger.dto.action.Request;
import com.gmail.ivanjermakov1.messenger.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.entity.Document;
import com.gmail.ivanjermakov1.messenger.entity.Image;
import com.gmail.ivanjermakov1.messenger.entity.Message;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidEntityException;
import com.gmail.ivanjermakov1.messenger.mapper.ConversationMapper;
import com.gmail.ivanjermakov1.messenger.mapper.MessageMapper;
import com.gmail.ivanjermakov1.messenger.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.repository.DocumentRepository;
import com.gmail.ivanjermakov1.messenger.repository.MessageRepository;
import com.gmail.ivanjermakov1.messenger.util.Threads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class MessagingService {

	private final static Logger LOG = LoggerFactory.getLogger(MessagingService.class);

	private final MessageService messageService;
	private final ConversationService conversationService;
	private final ImageService imageService;
	private final UserService userService;

	private final MessageRepository messageRepository;
	private final DocumentRepository documentRepository;

	private final UserMapper userMapper;
	private ConversationMapper conversationMapper;
	private MessageMapper messageMapper;

	private final List<Request<Action>> requests = new CopyOnWriteArrayList<>();

	@Value("${sse.timeout}")
	private Long sseTimeout;

	@Autowired
	public MessagingService(MessageService messageService, ConversationService conversationService, ImageService imageService, DocumentService documentService, UserMapper userMapper, UserService userService, MessageRepository messageRepository, DocumentRepository documentRepository) {
		this.messageService = messageService;
		this.conversationService = conversationService;
		this.imageService = imageService;
		this.userMapper = userMapper;
		this.userService = userService;
		this.messageRepository = messageRepository;
		this.documentRepository = documentRepository;
	}

	@Autowired
	public void setConversationMapper(ConversationMapper conversationMapper) {
		this.conversationMapper = conversationMapper;
	}

	@Autowired
	public void setMessageMapper(MessageMapper messageMapper) {
		this.messageMapper = messageMapper;
	}

	public void connect(Request<Action> request) {
		requests.add(request);
		Threads.timeout(
				() -> {
					LOG.debug("expired request of user @" + request.user.id);
					if (requests.remove(request)) {
						LOG.debug("clear expired request of user @" + request.user.id);
					} else {
						LOG.debug("not found expired request of user @" + request.user.id);
					}
					LOG.debug("requests payload size: " + requests.size());
				},
				sseTimeout
		);
	}

	public void disconnect(Request<Action> request) {
		requests.remove(request);
	}

	public MessageDto processNewMessage(User user, NewMessageDto newMessageDto) throws InvalidEntityException, AuthorizationException {
		Conversation conversation = conversationService.get(newMessageDto.conversationId);

		if (conversation.userConversations
				.stream()
				.filter(uc -> uc.user.id.equals(user.id))
				.noneMatch(uc -> uc.kicked.equals(false)))
			throw new AuthorizationException("no access");

		LOG.debug("process new message from @" + user.login + " to conversation @" + newMessageDto.conversationId +
				"; text: " + newMessageDto.text);

		Message message = new Message(
				conversation,
				LocalDateTime.now(),
				newMessageDto.text,
				user,
				newMessageDto.forwarded
						.stream()
						.map(dto -> messageService.get(dto.id))
						.collect(Collectors.toList()),
				new ArrayList<>(),
				newMessageDto.documents
						.stream()
						.map(d -> new Document(user, d.path, LocalDate.now()))
						.collect(Collectors.toList())
		);

		message.images = newMessageDto.images
				.stream()
				.map(i -> new Image(user, message, i.path, LocalDate.now()))
				.collect(Collectors.toList());

		if (!message.validate()) throw new InvalidEntityException("invalid message");

		messageRepository.save(message);

		MessageDto messageDto = messageMapper.with(user).map(message);

		forEachRequest(conversation, request -> sendResponse(request, new NewMessageAction(messageDto)));

		return messageDto;
	}

	public MessageDto processMessageEdit(User user, EditMessageDto editMessageDto) throws AuthenticationException, AuthorizationException {
		LOG.debug("process message edit @" + editMessageDto.id + "; text: " + editMessageDto.text);

		Message original = messageService.get(editMessageDto.id);

		if (original == null || !original.sender.id.equals(user.id))
			throw new AuthenticationException("user can edit only own messages");

		original.text = editMessageDto.text;
		if (editMessageDto.forwarded != null && editMessageDto.forwarded.isEmpty())
			messageRepository.deleteForwarded(original.id);

		original.images
				.stream()
				.filter(i -> editMessageDto.images
						.stream()
						.noneMatch(ei -> ei.id.equals(i.id)))
				.forEach(i -> imageService.delete(user, i.id));

		original.documents
				.stream()
				.filter(d -> editMessageDto.documents
						.stream()
						.noneMatch(ed -> ed.id.equals(d.id)))
				.forEach(documentRepository::delete);

		original = messageRepository.save(original);

		Conversation conversation = conversationService.get(original.conversation.id);
		MessageDto messageDto = messageMapper.with(user).map(original);

		forEachRequest(conversation, request -> sendResponse(request, new MessageEditAction(messageDto)));

		return messageDto;
	}

	public void processConversationRead(User user, Long conversationId) {
		Conversation conversation = conversationService.get(conversationId);

		messageService.read(user, conversation);

		forEachRequest(conversation, request -> sendResponse(
				request,
				new ConversationReadAction(
						conversationMapper
								.with(user)
								.map(conversation),
						userMapper.map(user)
				)
		));
	}

	public void systemMessage(NewMessageDto newMessageDto) throws InvalidEntityException {
		Conversation conversation = conversationService.get(newMessageDto.conversationId);
		User system = userService.getUser(newMessageDto.senderId);

		Message message = new Message(
				conversation,
				LocalDateTime.now(),
				newMessageDto.text,
				system,
				Collections.emptyList(),
				Collections.emptyList(),
				Collections.emptyList()
		);

		if (!message.validate()) throw new InvalidEntityException("invalid message");

		message = messageRepository.save(message);
		MessageDto messageDto = messageMapper.with(system).map(message);

		forEachRequest(conversation, request -> sendResponse(request, new NewMessageAction(messageDto)));
	}

	private void forEachRequest(Conversation conversation, Consumer<Request<Action>> consumer) {
		new Thread(() -> requests
				.stream()
				.filter(r -> conversation.userConversations
						.stream()
						.filter(uc -> !uc.kicked)
						.map(uc -> uc.user)
						.anyMatch(i -> i.id.equals(r.user.id)))
				.forEach(consumer)).start();
	}

	private void sendResponse(Request<Action> request, Action action) {
		LOG.debug("sending response to @" + request.user.login + "; type: " + action.type);
		request.listener.next(action);
	}

}
