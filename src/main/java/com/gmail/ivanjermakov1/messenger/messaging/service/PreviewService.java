package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDTO;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
	public PreviewService(ConversationService conversationService, UserInfoService userInfoService, MessageService messageService, UserService userService) {
		this.conversationService = conversationService;
		this.messageService = messageService;
		this.userService = userService;
	}
	
	public List<PreviewDTO> all(User user) {
		return allConversations(user)
				.stream()
				.map(c -> getPreview(user, c))
				.filter(p -> p.getLastMessage() != null)
				.sorted((p1, p2) -> p2.getLastMessage().getSent()
						.compareTo(p1.getLastMessage().getSent()))
				.collect(Collectors.toList());
	}
	
	private List<Conversation> allConversations(User user) {
		return new ArrayList<>(conversationService.getConversations(user));
	}
	
	public PreviewDTO getPreview(User user, Conversation conversation) {
		PreviewDTO previewDTO = new PreviewDTO();
		
		previewDTO.setConversation(conversationService.get(conversation));
		Message lastMessage = messageService.getLastMessage(conversation.getId());
		if (lastMessage != null) previewDTO.setLastMessage(messageService.getFullMessage(lastMessage));
		
		User with = conversation.getUsers()
				.stream()
				.filter(u -> !u.getId().equals(user.getId()))
				.findFirst()
				.orElse(user);
		previewDTO.setWith(userService.full(with));
		previewDTO.setUnread(messageService.unreadCount(user, conversation));
		
		return previewDTO;
	}
	
}
