package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PreviewService {
	
	private final ConversationService conversationService;
	private final MessageService messageService;
	private final UserService userService;
	private final MessageRepository messageRepository;
	
	@Autowired
	public PreviewService(ConversationService conversationService, MessageService messageService, UserService userService, MessageRepository messageRepository) {
		this.conversationService = conversationService;
		this.messageService = messageService;
		this.userService = userService;
		this.messageRepository = messageRepository;
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
		
		previewDto.setConversation(conversationService.get(user, conversation));
		messageRepository.getTop1ByConversationOrderBySentDesc(conversation)
				.ifPresent(lastMessage -> previewDto.setLastMessage(messageService.getFullMessage(user, lastMessage)));
		
		User with = conversation.getUsers()
				.stream()
				.filter(u -> !u.getId().equals(user.getId()))
				.findFirst()
				.orElse(user);
		previewDto.setWith(userService.full(with));
		previewDto.setUnread(conversationService.unreadCount(user, conversation));
		
		return previewDto;
	}
	
}
