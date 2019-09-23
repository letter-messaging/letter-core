package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.mapper.PreviewMapper;
import com.gmail.ivanjermakov1.messenger.service.ConversationService;
import com.gmail.ivanjermakov1.messenger.service.PreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("preview")
@Transactional
public class PreviewControllerImpl implements PreviewController {

	private final PreviewService previewService;
	private final ConversationService conversationService;

	private final PreviewMapper previewMapper;

	@Autowired
	public PreviewControllerImpl(PreviewService previewService, ConversationService conversationService, PreviewMapper previewMapper) {
		this.previewService = previewService;
		this.conversationService = conversationService;
		this.previewMapper = previewMapper;
	}

	@Override
	@GetMapping("all")
	public List<PreviewDto> all(@ModelAttribute User user,
	                            Pageable pageable) {
		return previewService.all(user, pageable);
	}

	@Override
	@GetMapping("get")
	public PreviewDto get(@ModelAttribute User user,
	                      @RequestParam("conversationId") Long conversationId) {
		Conversation conversation = conversationService.get(conversationId);

		PreviewDto preview = previewMapper.with(user).map(conversation);
		conversationService.show(user, conversation);

		return preview;
	}

}
