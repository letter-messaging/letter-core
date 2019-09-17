package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.service.MessageService;
import com.gmail.ivanjermakov1.messenger.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("message")
@Transactional
public class MessageControllerImpl implements MessageController {

	private final MessageService messageService;
	private final MessagingService messagingService;

	@Autowired
	public MessageControllerImpl(MessageService messageService, MessagingService messagingService) {
		this.messageService = messageService;
		this.messagingService = messagingService;
	}

	@Override
	@GetMapping("get")
	public List<MessageDto> get(@ModelAttribute User user,
	                            @RequestParam("conversationId") Long conversationId,
	                            @PageableDefault(direction = Sort.Direction.DESC, sort = {"sent"}) Pageable pageable) {
		messagingService.processConversationRead(user, conversationId);
		return messageService.get(user.id, conversationId, pageable);
	}

	@Override
	@PostMapping("delete")
	public void delete(@ModelAttribute User user,
	                   @RequestBody List<MessageDto> deleteMessages) {
		messageService.delete(user, deleteMessages);
	}

}
