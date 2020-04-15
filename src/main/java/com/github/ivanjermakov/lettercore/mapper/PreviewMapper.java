package com.github.ivanjermakov.lettercore.mapper;

import com.github.ivanjermakov.lettercore.dto.AvatarDto;
import com.github.ivanjermakov.lettercore.dto.PreviewDto;
import com.github.ivanjermakov.lettercore.dto.enums.PreviewType;
import com.github.ivanjermakov.lettercore.entity.Conversation;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.service.ConversationService;
import com.github.ivanjermakov.lettercore.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class PreviewMapper implements Mapper<Conversation, PreviewDto>, MapperBuilder<User> {

	@Value("${default.avatar.chat.path}")
	private String defaultAvatarChatPath;

	private MessageService messageService;
	private ConversationService conversationService;

	private UserMapper userMapper;
	private ConversationMapper conversationMapper;

	private User user;

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	@Autowired
	public void setConversationService(ConversationService conversationService) {
		this.conversationService = conversationService;
	}

	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Autowired
	public void setConversationMapper(ConversationMapper conversationMapper) {
		this.conversationMapper = conversationMapper;
	}

	@Override
	public PreviewDto map(Conversation conversation) {
		PreviewDto previewDto = new PreviewDto();

		previewDto.type = conversation.chatName == null ? PreviewType.CONVERSATION : PreviewType.CHAT;

		previewDto.conversation = conversationMapper
				.with(user)
				.map(conversation);

		previewDto.lastMessage = messageService.get(user.id, conversation.id, PageRequest.of(0, 1))
				.stream()
				.findFirst()
				.orElse(null);

		if (previewDto.type.equals(PreviewType.CONVERSATION)) {
			User with = conversation.userConversations
					.stream()
					.map(uc -> uc.user)
					.filter(u -> !u.id.equals(user.id))
					.findFirst()
					.orElse(user);
			previewDto.with = userMapper.map(with);
		}

		if (previewDto.type.equals(PreviewType.CONVERSATION)) {
			previewDto.avatar = previewDto.with.avatar;
		} else {
			previewDto.avatar = new AvatarDto(null, defaultAvatarChatPath, null);
		}

		if (previewDto.type.equals(PreviewType.CHAT)) {
			previewDto.kicked = conversation.userConversations
					.stream()
					.filter(uc -> uc.user.id.equals(user.id))
					.findFirst()
					.map(uc -> uc.kicked)
					.orElse(null);
		}

		previewDto.unread = conversationService.unreadCount(user, conversation);

		return previewDto;
	}

	@Override
	public Mapper<Conversation, PreviewDto> with(User user) {
		this.user = user;
		return this;
	}

}
