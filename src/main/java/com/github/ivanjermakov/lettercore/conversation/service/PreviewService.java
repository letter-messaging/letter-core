package com.github.ivanjermakov.lettercore.conversation.service;

import com.github.ivanjermakov.lettercore.conversation.dto.PreviewDto;
import com.github.ivanjermakov.lettercore.conversation.mapper.ConversationMapper;
import com.github.ivanjermakov.lettercore.conversation.mapper.PreviewMapper;
import com.github.ivanjermakov.lettercore.messaging.service.MessageService;
import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.user.mapper.UserMapper;
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

	private final PreviewMapper previewMapper;

	@Autowired
	public PreviewService(ConversationService conversationService, UserMapper userMapper, MessageService messageService, PreviewMapper previewMapper) {
		this.conversationService = conversationService;
		this.previewMapper = previewMapper;
	}

	@Autowired
	public void setConversationMapper(ConversationMapper conversationMapper) {
	}

	public List<PreviewDto> all(User user, Pageable pageable) {
		return conversationService.getConversations(user, PageRequest.of(0, Integer.MAX_VALUE))
				.stream()
				.map(c -> previewMapper.with(user).map(c))
				.filter(p -> p.lastMessage != null)
				.sorted(Comparator.comparing(p -> p.lastMessage.sent, Comparator.reverseOrder()))
				.skip(pageable.getOffset())
				.limit(pageable.getPageSize())
				.collect(Collectors.toList());
	}

}
