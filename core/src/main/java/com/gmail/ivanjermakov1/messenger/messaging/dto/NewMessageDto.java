package com.gmail.ivanjermakov1.messenger.messaging.dto;

import java.util.List;

public class NewMessageDto {
	
	private Long senderId;
	private Long conversationId;
	private String text;
	private List<MessageDto> forwarded;
	private List<NewImageDto> images;
	
	public NewMessageDto() {
	}
	
	public NewMessageDto(Long senderId, Long conversationId, String text, List<MessageDto> forwarded, List<NewImageDto> images) {
		this.senderId = senderId;
		this.conversationId = conversationId;
		this.text = text;
		this.forwarded = forwarded;
		this.images = images;
	}
	
	public Long getSenderId() {
		return senderId;
	}
	
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	
	public Long getConversationId() {
		return conversationId;
	}
	
	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public List<MessageDto> getForwarded() {
		return forwarded;
	}
	
	public void setForwarded(List<MessageDto> forwarded) {
		this.forwarded = forwarded;
	}
	
	public List<NewImageDto> getImages() {
		return images;
	}
	
	public void setImages(List<NewImageDto> images) {
		this.images = images;
	}
	
}
