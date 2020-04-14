package com.gmail.ivanjermakov1.messenger.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewMessageDto {

	public Long senderId;
	public Long conversationId;
	public String text;
	public List<MessageDto> forwarded;
	public List<NewImageDto> images;
	public List<NewDocumentDto> documents;

	public NewMessageDto() {
	}

	public NewMessageDto(Long senderId, Long conversationId, String text) {
		this(
				senderId,
				conversationId,
				text,
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>()
		);
	}

	public NewMessageDto(Long senderId, Long conversationId, String text, List<MessageDto> forwarded, List<NewImageDto> images, List<NewDocumentDto> documents) {
		this.senderId = senderId;
		this.conversationId = conversationId;
		this.text = text;
		this.forwarded = forwarded;
		this.images = images;
		this.documents = documents;
	}

	public static NewMessageDto systemMessage(Long conversationId, String text) {
		return new NewMessageDto(
				0L,
				conversationId,
				text,
				Collections.emptyList(),
				Collections.emptyList(),
				Collections.emptyList()
		);
	}

}
