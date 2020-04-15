package com.github.ivanjermakov.lettercore.messaging.mapper;

import com.github.ivanjermakov.lettercore.common.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.common.mapper.Mapper;
import com.github.ivanjermakov.lettercore.common.mapper.MapperBuilder;
import com.github.ivanjermakov.lettercore.conversation.entity.Conversation;
import com.github.ivanjermakov.lettercore.conversation.entity.UserConversation;
import com.github.ivanjermakov.lettercore.conversation.mapper.ConversationMapper;
import com.github.ivanjermakov.lettercore.conversation.repository.UserConversationRepository;
import com.github.ivanjermakov.lettercore.conversation.service.ConversationService;
import com.github.ivanjermakov.lettercore.file.dto.DocumentDto;
import com.github.ivanjermakov.lettercore.file.dto.ImageDto;
import com.github.ivanjermakov.lettercore.messaging.dto.MessageDto;
import com.github.ivanjermakov.lettercore.messaging.entity.Message;
import com.github.ivanjermakov.lettercore.messaging.repository.MessageRepository;
import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.user.mapper.UserMapper;
import com.github.ivanjermakov.lettercore.user.service.UserService;
import com.github.ivanjermakov.lettercore.util.Mappers;
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
