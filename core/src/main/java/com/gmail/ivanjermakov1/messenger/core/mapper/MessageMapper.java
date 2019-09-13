package com.gmail.ivanjermakov1.messenger.core.mapper;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.core.util.Mappers;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.DocumentDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ImageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserConversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.MessageRepository;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserConversationRepository;
import com.gmail.ivanjermakov1.messenger.messaging.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class MessageMapper implements Mapper<Message, MessageDto>, MapperBuilder<User> {

	private UserConversationRepository userConversationRepository;
	private ConversationService conversationService;
	private ConversationMapper conversationMapper;
	private UserService userService;
	private UserMapper userMapper;
	private MessageRepository messageRepository;

	private User user;

	@Autowired
	public void setUserConversationRepository(UserConversationRepository userConversationRepository) {
		this.userConversationRepository = userConversationRepository;
	}

	@Autowired
	public void setConversationService(ConversationService conversationService) {
		this.conversationService = conversationService;
	}

	@Autowired
	public void setConversationMapper(ConversationMapper conversationMapper) {
		this.conversationMapper = conversationMapper;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Autowired
	public void setMessageRepository(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@Override
	public MessageDto map(Message message) {
		if (message.sender.id.equals(0L)) {
			return new MessageDto(
					message.id,
					message.sent,
					message.text,
					true,
					userMapper.map(message.sender),
					conversationMapper.with(user).map(message.conversation),
					Collections.emptyList(),
					Collections.emptyList(),
					Collections.emptyList()
			);
		}

		UserConversation userConversation = userConversationRepository.findByUserAndConversation(user, message.conversation)
				.orElseThrow(() -> new NoSuchEntityException("no such user's conversation"));

		MessageDto messageDto = new MessageDto();
		messageDto.id = message.id;
		messageDto.sent = message.sent;
		boolean read = userConversation.conversation.userConversations
				.stream()
				.filter(uc -> !uc.user.id.equals(user.id))
				.anyMatch(uc -> uc.lastRead.isAfter(message.sent));
		if (userConversation.conversation.userConversations.size() == 1) read = true;
		messageDto.read = read;
		messageDto.text = message.text;

		Conversation conversation = conversationService.get(message.conversation.id);
		messageDto.conversation = conversationMapper
				.with(message.sender)
				.map(conversation);

		User sender = userService.getUser(message.sender.id);
		messageDto.sender = userMapper.map(sender);

		messageDto.forwarded = messageRepository.getById(message.id)
				.map(m -> m.forwarded)
				.orElse(Collections.emptyList())
				.stream()
				.map(m -> this.with(user).map(m))
				.collect(Collectors.toList());

		messageDto.images = Mappers.mapAll(
				message.images,
				ImageDto.class
		);

		messageDto.documents = Mappers.mapAll(
				message.documents,
				DocumentDto.class
		);

		return messageDto;
	}

	@Override
	public Mapper<Message, MessageDto> with(User user) {
		this.user = user;
		return this;
	}

}
