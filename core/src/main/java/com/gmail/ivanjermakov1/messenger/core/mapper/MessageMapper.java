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
		if (message.getSender().getId().equals(0L)) {
			return new MessageDto(
					message.getId(),
					message.getSent(),
					message.getText(),
					true,
					userMapper.map(message.getSender()),
					conversationMapper.with(user).map(message.getConversation()),
					Collections.emptyList(),
					Collections.emptyList(),
					Collections.emptyList()
			);
		}
		
		UserConversation userConversation = userConversationRepository.findByUserAndConversation(user, message.getConversation())
				.orElseThrow(() -> new NoSuchEntityException("no such user's conversation"));
		
		MessageDto messageDto = new MessageDto();
		messageDto.setId(message.getId());
		messageDto.setSent(message.getSent());
		boolean read = userConversation.getConversation().getUserConversations()
				.stream()
				.filter(uc -> !uc.getUser().getId().equals(user.getId()))
				.anyMatch(uc -> uc.getLastRead().isAfter(message.getSent()));
		if (userConversation.getConversation().getUserConversations().size() == 1) read = true;
		messageDto.setRead(read);
		messageDto.setText(message.getText());
		
		Conversation conversation = conversationService.get(message.getConversation().getId());
		messageDto.setConversation(conversationMapper
				.with(message.getSender())
				.map(conversation));
		
		User sender = userService.getUser(message.getSender().getId());
		messageDto.setSender(userMapper.map(sender));
		
		messageDto.setForwarded(
				messageRepository.getById(message.getId())
						.map(Message::getForwarded)
						.orElse(Collections.emptyList())
						.stream()
						.map(m -> this.with(user).map(m))
						.collect(Collectors.toList())
		);
		
		messageDto.setImages(Mappers.mapAll(
				message.getImages(),
				ImageDto.class
		));
		
		messageDto.setDocuments(Mappers.mapAll(
				message.getDocuments(),
				DocumentDto.class
		));
		
		return messageDto;
	}
	
	@Override
	public Mapper<Message, MessageDto> with(User user) {
		this.user = user;
		return this;
	}
	
}
