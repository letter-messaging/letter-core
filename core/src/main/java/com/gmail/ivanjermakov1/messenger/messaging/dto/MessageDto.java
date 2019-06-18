package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

public class MessageDto {
	
	private Long id;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime sent;
	
	private String text;
	private Boolean read;
	private UserDto sender;
	private ConversationDto conversation;
	private List<MessageDto> forwarded;
	private List<ImageDto> images;
	private List<DocumentDto> documents;
	
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
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDateTime getSent() {
		return sent;
	}
	
	public void setSent(LocalDateTime sent) {
		this.sent = sent;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Boolean getRead() {
		return read;
	}
	
	public void setRead(Boolean read) {
		this.read = read;
	}
	
	public UserDto getSender() {
		return sender;
	}
	
	public void setSender(UserDto sender) {
		this.sender = sender;
	}
	
	public ConversationDto getConversation() {
		return conversation;
	}
	
	public void setConversation(ConversationDto conversation) {
		this.conversation = conversation;
	}
	
	public List<MessageDto> getForwarded() {
		return forwarded;
	}
	
	public void setForwarded(List<MessageDto> forwarded) {
		this.forwarded = forwarded;
	}
	
	public List<ImageDto> getImages() {
		return images;
	}
	
	public void setImages(List<ImageDto> images) {
		this.images = images;
	}
	
	public List<DocumentDto> getDocuments() {
		return documents;
	}
	
	public void setDocuments(List<DocumentDto> documents) {
		this.documents = documents;
	}
	
}
