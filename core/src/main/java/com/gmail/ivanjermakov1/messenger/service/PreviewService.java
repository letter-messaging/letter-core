package com.gmail.ivanjermakov1.messenger.service;

import com.gmail.ivanjermakov1.messenger.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.mapper.ConversationMapper;
import com.gmail.ivanjermakov1.messenger.mapper.PreviewMapper;
import com.gmail.ivanjermakov1.messenger.mapper.UserMapper;
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
