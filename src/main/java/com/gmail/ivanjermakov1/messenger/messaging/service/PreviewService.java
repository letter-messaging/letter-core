package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.FullUser;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Preview;
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
	private final UserMainInfoService userMainInfoService;
	private final MessageService messageService;
	
	@Autowired
	public PreviewService(ConversationService conversationService, UserMainInfoService userMainInfoService, MessageService messageService) {
		this.conversationService = conversationService;
		this.userMainInfoService = userMainInfoService;
		this.messageService = messageService;
	}
	
	public List<Preview> all(User user) {
		return allConversations(user)
				.stream()
				.map(c -> getPreview(user, c))
				.collect(Collectors.toList());
	}
	
	private List<Conversation> allConversations(User user) {
		return new ArrayList<>(conversationService.getConversations(user));
	}
	
	public Preview getPreview(User user, Conversation conversation) {
		Preview preview = new Preview();
		
		preview.setConversation(conversation);
		Message lastMessage = messageService.getLastMessage(conversation.getId());
		if (lastMessage != null) preview.setLastMessage(messageService.getFullMessage(lastMessage));
		
		User with = conversation.getUsers()
				.stream()
				.filter(u -> !u.getId().equals(user.getId()))
				.findFirst()
				.orElse(user);
		preview.setWith(new FullUser(with, userMainInfoService.getById(with.getId())));
		
		return preview;
	}
	
}
