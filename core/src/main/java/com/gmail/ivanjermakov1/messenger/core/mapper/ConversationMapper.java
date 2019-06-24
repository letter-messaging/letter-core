package com.gmail.ivanjermakov1.messenger.core.mapper;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserConversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ConversationMapper implements Mapper<Conversation, ConversationDto>, MapperBuilder<User> {
	
	private UserConversationRepository userConversationRepository;
	private UserMapper userMapper;
	
	private User user;
	
	@Autowired
	public void setUserConversationRepository(UserConversationRepository userConversationRepository) {
		this.userConversationRepository = userConversationRepository;
	}
	
	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
	@Override
	public ConversationDto map(Conversation conversation) {
		Optional<UserConversation> userConversation = userConversationRepository.findByUserAndConversation(user, conversation);
		return new ConversationDto(
				conversation.getId(),
				conversation.getChatName(),
				userConversation.map(UserConversation::getHidden).orElse(null),
				conversation.getUserConversations()
						.stream()
						.map(UserConversation::getUser)
						.map(userMapper::map)
						.collect(Collectors.toList())
		);
	}
	
	@Override
	public ConversationMapper with(User user) {
		this.user = user;
		return this;
	}
	
}
