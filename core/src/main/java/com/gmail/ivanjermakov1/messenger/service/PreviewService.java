package com.gmail.ivanjermakov1.messenger.service;

import com.gmail.ivanjermakov1.messenger.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.dto.enums.PreviewType;
import com.gmail.ivanjermakov1.messenger.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.mapper.ConversationMapper;
import com.gmail.ivanjermakov1.messenger.mapper.UserMapper;
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

}
