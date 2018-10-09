package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Preview;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserMainInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
		return allIds(user).stream()
				.map(conversationId -> getPreview(user, conversationId))
				.collect(Collectors.toList());
	}
	
	private List<Long> allIds(User user) {
		return new ArrayList<>(conversationService.allIds(user));
	}
	
	public Preview getPreview(User user, Long conversationId) {
		Preview preview = new Preview();
		
		preview.setConversationId(conversationId);
		preview.setSenderId(user.getId());
		
		UserMainInfo userMainInfo = userMainInfoService.getById(user.getId());
		
		preview.setFirstName(userMainInfo.getFirstName());
		preview.setLastName(userMainInfo.getLastName());
		
		Message lastMessage = messageService.getLastMessage(conversationId);
		
		preview.setLastMessage(lastMessage);
		
		return preview;
	}
	
}
