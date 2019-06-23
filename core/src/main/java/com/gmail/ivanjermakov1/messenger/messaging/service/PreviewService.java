package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.core.mapper.ConversationMapper;
import com.gmail.ivanjermakov1.messenger.core.mapper.MessageMapper;
import com.gmail.ivanjermakov1.messenger.core.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserConversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
	private final MessageRepository messageRepository;
	
	private final UserMapper userMapper;
	private ConversationMapper conversationMapper;
	private MessageMapper messageMapper;
	
	@Autowired
	public PreviewService(ConversationService conversationService, MessageService messageService, MessageRepository messageRepository, UserMapper userMapper) {
		this.conversationService = conversationService;
		this.messageService = messageService;
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
		
		previewDto.setConversation(conversationMapper
				.with(user)
				.map(conversation));
		
		messageRepository.getTop1ByConversationOrderBySentDesc(conversation)
				.ifPresent(lastMessage -> previewDto.setLastMessage(messageMapper.with(user).map(lastMessage)));
		
		User with = conversation.getUserConversations()
				.stream()
				.map(UserConversation::getUser)
				.filter(u -> !u.getId().equals(user.getId()))
				.findFirst()
				.orElse(user);
		previewDto.setWith(userMapper.map(with));
		previewDto.setUnread(conversationService.unreadCount(user, conversation));
		
		return previewDto;
	}
	
}
