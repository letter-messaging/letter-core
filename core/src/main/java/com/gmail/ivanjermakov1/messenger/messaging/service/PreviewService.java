package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.core.mapper.ConversationMapper;
import com.gmail.ivanjermakov1.messenger.core.mapper.MessageMapper;
import com.gmail.ivanjermakov1.messenger.core.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.messaging.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.enums.PreviewType;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserConversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.MessageRepository;
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
	private final MessageRepository messageRepository;
	
	private final UserMapper userMapper;
	private ConversationMapper conversationMapper;
	private MessageMapper messageMapper;
	
	@Value("${default.avatar.chat.path}")
	private String defaultAvatarChatPath;
	
	@Autowired
	public PreviewService(ConversationService conversationService, MessageRepository messageRepository, UserMapper userMapper) {
		this.conversationService = conversationService;
		this.messageRepository = messageRepository;
		this.userMapper = userMapper;
	}
	
	@Autowired
	public void setConversationMapper(ConversationMapper conversationMapper) {
		this.conversationMapper = conversationMapper;
	}
	
	@Autowired
	public void setMessageMapper(MessageMapper messageMapper) {
		this.messageMapper = messageMapper;
	}
	
	public List<PreviewDto> all(User user, Pageable pageable) {
		return conversationService.getConversations(user, PageRequest.of(0, Integer.MAX_VALUE))
				.stream()
				.map(c -> getPreview(user, c))
				.filter(p -> p.getLastMessage() != null)
				.sorted(Comparator.comparing(p -> p.getLastMessage().getSent(), Comparator.reverseOrder()))
				.skip(pageable.getOffset())
				.limit(pageable.getPageSize())
				.collect(Collectors.toList());
	}
	
	public PreviewDto getPreview(User user, Conversation conversation) {
		PreviewDto previewDto = new PreviewDto();
		
		previewDto.setType(conversation.getChatName() == null ? PreviewType.CONVERSATION : PreviewType.CHAT);
		
		previewDto.setConversation(conversationMapper
				.with(user)
				.map(conversation));
		
		messageRepository.getTop1ByConversationOrderBySentDesc(conversation)
				.ifPresent(lastMessage -> previewDto.setLastMessage(messageMapper.with(user).map(lastMessage)));
		
		if (previewDto.getType().equals(PreviewType.CONVERSATION)) {
			User with = conversation.getUserConversations()
					.stream()
					.map(UserConversation::getUser)
					.filter(u -> !u.getId().equals(user.getId()))
					.findFirst()
					.orElse(user);
			previewDto.setWith(userMapper.map(with));
		}
		
		if (previewDto.getType().equals(PreviewType.CONVERSATION)) {
			previewDto.setAvatar(previewDto.getWith().getAvatar());
		} else {
			previewDto.setAvatar(new AvatarDto(null, defaultAvatarChatPath, null));
		}
		
		previewDto.setUnread(conversationService.unreadCount(user, conversation));
		
		return previewDto;
	}
	
}
