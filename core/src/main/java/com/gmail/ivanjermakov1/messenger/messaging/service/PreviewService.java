package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.core.mapper.ConversationMapper;
import com.gmail.ivanjermakov1.messenger.core.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.messaging.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.enums.PreviewType;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserConversation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PreviewService {

	private final ConversationService conversationService;
	private final MessageService messageService;

	private final UserMapper userMapper;
	private ConversationMapper conversationMapper;

	@Value("${default.avatar.chat.path}")
	private String defaultAvatarChatPath;

	@Autowired
	public PreviewService(ConversationService conversationService, UserMapper userMapper, MessageService messageService) {
		this.conversationService = conversationService;
		this.userMapper = userMapper;
		this.messageService = messageService;
	}

	@Autowired
	public void setConversationMapper(ConversationMapper conversationMapper) {
		this.conversationMapper = conversationMapper;
	}

	public List<PreviewDto> all(User user, Pageable pageable) {
		return conversationService.getConversations(user, PageRequest.of(0, Integer.MAX_VALUE))
				.stream()
				.map(c -> getPreview(user, c))
				.filter(p -> p.lastMessage != null)
				.sorted(Comparator.comparing(p -> p.lastMessage.sent, Comparator.reverseOrder()))
				.skip(pageable.getOffset())
				.limit(pageable.getPageSize())
				.collect(Collectors.toList());
	}

	public PreviewDto getPreview(User user, Conversation conversation) {
		PreviewDto previewDto = new PreviewDto();

		previewDto.type = conversation.getChatName() == null ? PreviewType.CONVERSATION : PreviewType.CHAT;

		previewDto.conversation = conversationMapper
				.with(user)
				.map(conversation);

		previewDto.lastMessage = messageService.get(user.getId(), conversation.getId(), PageRequest.of(0, 1))
				.stream()
				.findFirst()
				.orElse(null);

		if (previewDto.type.equals(PreviewType.CONVERSATION)) {
			User with = conversation.getUserConversations()
					.stream()
					.map(UserConversation::getUser)
					.filter(u -> !u.getId().equals(user.getId()))
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
			previewDto.kicked = conversation.getUserConversations()
					.stream()
					.filter(uc -> uc.getUser().getId().equals(user.getId()))
					.findFirst()
					.map(UserConversation::getKicked)
					.orElse(null);
		}

		previewDto.unread = conversationService.unreadCount(user, conversation);

		return previewDto;
	}

}
