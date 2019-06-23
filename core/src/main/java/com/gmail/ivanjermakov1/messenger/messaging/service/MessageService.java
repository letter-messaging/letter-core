package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.core.util.Mapper;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.DocumentDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ImageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserConversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.MessageRepository;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService {
	
	private final MessageRepository messageRepository;
	private final UserService userService;
	private ConversationService conversationService;
	private final ImageService imageService;
	private final UserConversationRepository userConversationRepository;
	
	@Autowired
	public MessageService(MessageRepository messageRepository, UserService userService, ImageService imageService, UserConversationRepository userConversationRepository) {
		this.messageRepository = messageRepository;
		this.userService = userService;
		this.imageService = imageService;
		this.userConversationRepository = userConversationRepository;
	}
	
	@Autowired
	public void setConversationService(ConversationService conversationService) {
		this.conversationService = conversationService;
	}
	
	public Message save(Message message) {
		return messageRepository.save(message);
	}
	
	public List<MessageDto> get(Long userId, Long conversationId, Pageable pageable) throws AuthenticationException {
		User user = userService.getUser(userId);
		Conversation conversation = conversationService.get(conversationId);
		
		if (conversation.getUsers().stream().noneMatch(u -> u.getId().equals(userId)))
			throw new AuthenticationException("invalid conversation id");
		
		List<Message> messagesIds = messageRepository.findAllByConversation(conversation, pageable);
		
		return messagesIds
				.stream()
				.map(message -> getFullMessage(user, message))
				.collect(Collectors.toList());
	}
	
	public MessageDto getFullMessage(User user, Message message) {
		UserConversation userConversation = userConversationRepository.findByUserAndConversation(user, message.getConversation())
				.orElseThrow(() -> new NoSuchEntityException("no such user's conversation"));
		
		MessageDto messageDto = new MessageDto();
		messageDto.setId(message.getId());
		messageDto.setSent(message.getSent());
		boolean read = userConversation.getConversation().getUserConversations()
				.stream()
				.anyMatch(uc -> uc.getLastRead().isAfter(message.getSent()));
		messageDto.setRead(read);
		messageDto.setText(message.getText());
		
		Conversation conversation = conversationService.get(message.getConversation().getId());
		messageDto.setConversation(conversationService.get(message.getSender(), conversation));
		
		User sender = userService.getUser(message.getSender().getId());
		messageDto.setSender(userService.full(sender));
		
		messageDto.setForwarded(Optional
				.ofNullable(messageRepository.getById(message.getId()).getForwarded())
				.orElse(Collections.emptyList())
				.stream()
				.map(m -> getFullMessage(user, m))
				.collect(Collectors.toList()));
		
		messageDto.setImages(Mapper.mapAll(
				message.getImages(),
				ImageDto.class
		));
		
		messageDto.setDocuments(Mapper.mapAll(
				message.getDocuments(),
				DocumentDto.class
		));
		
		return messageDto;
	}
	
	public void read(User user, Conversation conversation) {
		UserConversation userConversation = userConversationRepository.findByUserAndConversation(user, conversation)
				.orElseThrow(() -> new NoSuchEntityException("no such user's conversation"));
		
		userConversation.setLastRead(LocalDateTime.now());
	}
	
	/**
	 * Allowed to delete only user-self messages
	 *
	 * @param user           messages owner
	 * @param deleteMessages messages which going to be removed
	 */
	public void delete(User user, List<MessageDto> deleteMessages) {
		deleteMessages
				.stream()
				.map(dto -> messageRepository.getById(dto.getId()))
				.filter(m -> m.getSender().getId().equals(user.getId()))
				.forEach(this::delete);
	}
	
	public void deleteForwarded(Message message) {
		messageRepository.deleteForwarded(message.getId());
	}
	
	public void deleteImages(Message message) {
		message.getImages().forEach(imageService::delete);
	}
	
	public Message get(Long messageId) {
		return messageRepository.getById(messageId);
	}
	
	public void deleteAll(User user, Conversation conversation) {
//		TODO: optimize
		messageRepository.getAllBySenderAndConversation(user, conversation).stream().parallel().forEach(this::delete);
	}
	
	/**
	 * Delete messages different way then just <code>.delete()</code>. Firstly deleting current message from all over
	 * forwarded messages (including originally attached images), then deletes itself
	 *
	 * @param message message that will be deleted
	 */
	public void delete(Message message) {
		messageRepository.deleteFromForwarded(message.getId());
		deleteImages(message);
		messageRepository.delete(message);
	}
	
}
