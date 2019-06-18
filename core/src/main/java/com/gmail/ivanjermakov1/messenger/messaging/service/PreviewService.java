package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PreviewService {
	
	private final ConversationService conversationService;
	private final MessageService messageService;
	private final UserService userService;
	
	@Autowired
	public PreviewService(ConversationService conversationService, MessageService messageService, UserService userService) {
		this.conversationService = conversationService;
		this.messageService = messageService;
		this.userService = userService;
	}
	
	public List<PreviewDto> all(User user, Pageable pageable) {
		return conversationService.getConversations(user, pageable)
				.stream()
				.map(c -> getPreview(user, c))
				.filter(p -> p.getLastMessage() != null)
				.sorted((p1, p2) -> p2.getLastMessage().getSent()
						.compareTo(p1.getLastMessage().getSent()))
				.collect(Collectors.toList());
	}
	
	public PreviewDto getPreview(User user, Conversation conversation) {
		PreviewDto previewDto = new PreviewDto();
		
		previewDto.setConversation(conversationService.get(user, conversation));
		Message lastMessage = messageService.getLastMessage(conversation.getId());
		if (lastMessage != null) previewDto.setLastMessage(messageService.getFullMessage(lastMessage));
		
		User with = conversation.getUsers()
				.stream()
				.filter(u -> !u.getId().equals(user.getId()))
				.findFirst()
				.orElse(user);
		previewDto.setWith(userService.full(with));
		previewDto.setUnread(messageService.unreadCount(user, conversation));
		
		return previewDto;
	}
	
}
