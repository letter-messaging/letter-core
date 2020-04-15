package com.github.ivanjermakov.lettercore.mapper;

import com.github.ivanjermakov.lettercore.dto.ConversationDto;
import com.github.ivanjermakov.lettercore.entity.Conversation;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.entity.UserConversation;
import com.github.ivanjermakov.lettercore.repository.UserConversationRepository;
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
				conversation.id,
				conversation.chatName,
				userConversation.map(uc -> uc.hidden).orElse(null),
				conversation.userConversations
						.stream()
						.filter(uc -> uc.kicked.equals(false))
						.map(uc -> uc.user)
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
