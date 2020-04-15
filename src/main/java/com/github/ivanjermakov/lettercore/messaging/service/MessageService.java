package com.github.ivanjermakov.lettercore.messaging.service;

import com.github.ivanjermakov.lettercore.common.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.conversation.entity.Conversation;
import com.github.ivanjermakov.lettercore.conversation.entity.UserConversation;
import com.github.ivanjermakov.lettercore.conversation.repository.UserConversationRepository;
import com.github.ivanjermakov.lettercore.conversation.service.ConversationService;
import com.github.ivanjermakov.lettercore.file.service.ImageService;
import com.github.ivanjermakov.lettercore.messaging.dto.MessageDto;
import com.github.ivanjermakov.lettercore.messaging.entity.Message;
import com.github.ivanjermakov.lettercore.messaging.mapper.MessageMapper;
import com.github.ivanjermakov.lettercore.messaging.repository.MessageRepository;
import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.user.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

	private final MessageRepository messageRepository;
	private final UserService userService;
	private ConversationService conversationService;
	private final ImageService imageService;
	private final UserConversationRepository userConversationRepository;

	private MessageMapper messageMapper;

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

	@Autowired
	public void setMessageMapper(MessageMapper messageMapper) {
		this.messageMapper = messageMapper;
	}

	public List<MessageDto> get(@NotNull Long userId, @NotNull Long conversationId, Pageable pageable) {
		User user = userService.getUser(userId);
		Conversation conversation = conversationService.get(conversationId);
		UserConversation userConversation = userConversationRepository.findByUserAndConversation(user, conversation)
				.orElseThrow(() -> new NoSuchEntityException("invalid conversation id"));

		List<Message> messages = messageRepository.findAllByConversationOrderBySentDesc(
				conversation,
				PageRequest.of(0, Integer.MAX_VALUE)
		);

		return messages
				.stream()
				.filter(m -> !(userConversation.kicked && userConversation.lastRead.isBefore(m.sent)))
				.skip(pageable.getOffset())
				.limit(pageable.getPageSize())
				.map(message -> messageMapper.with(user).map(message))
				.collect(Collectors.toList());
	}

	public void read(User user, Conversation conversation) {
		UserConversation userConversation = userConversationRepository.findByUserAndConversation(user, conversation)
				.orElseThrow(() -> new NoSuchEntityException("no such user's conversation"));

		if (!userConversation.kicked) {
			userConversation.lastRead = LocalDateTime.now();
		}
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
				.map(dto -> messageRepository.getById(dto.id))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.filter(m -> m.sender.id.equals(user.id))
				.forEach(this::delete);
	}

	public void deleteImages(Message message) {
		message.images.forEach(i -> imageService.delete(message.sender, i.id));
	}

	public Message get(Long messageId) {
		return messageRepository.getById(messageId)
				.orElseThrow(() -> new NoSuchEntityException("no such message"));
	}

	public void deleteAll(User user, Conversation conversation) {
//		TODO: optimize
		messageRepository.getAllBySenderAndConversation(user, conversation)
				.stream()
				.parallel()
				.forEach(this::delete);
	}

	/**
	 * Delete messages different way then just <code>.delete()</code>. Firstly deleting current message from all over
	 * forwarded messages (including originally attached images), then deletes itself
	 *
	 * @param message message that will be deleted
	 */
	public void delete(Message message) {
		messageRepository.deleteFromForwarded(message.id);
		deleteImages(message);
		messageRepository.delete(message);
	}

}
