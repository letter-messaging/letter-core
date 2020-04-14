package com.github.ivanjermakov.lettercore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class MessageDto {

	public Long id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public LocalDateTime sent;

	public String text;
	public Boolean read;
	public UserDto sender;
	public ConversationDto conversation;
	public List<MessageDto> forwarded;
	public List<ImageDto> images;
	public List<DocumentDto> documents;

	public MessageDto() {
	}

	public MessageDto(Long id, LocalDateTime sent, String text, Boolean read, UserDto sender, ConversationDto conversation, List<MessageDto> forwarded, List<ImageDto> images, List<DocumentDto> documents) {
		this.id = id;
		this.sent = sent;
		this.text = text;
		this.read = read;
		this.sender = sender;
		this.conversation = conversation;
		this.forwarded = forwarded;
		this.images = images;
		this.documents = documents;
	}

}
