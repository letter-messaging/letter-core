package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;

public class PreviewDTO {
	
	private Conversation conversation;
	private UserDTO with;
	private MessageDTO lastMessage;
	
	public PreviewDTO() {
	}
	
	public PreviewDTO(Conversation conversation, UserDTO with, MessageDTO lastMessage) {
		this.conversation = conversation;
		this.with = with;
		this.lastMessage = lastMessage;
	}
	
	public Conversation getConversation() {
		return conversation;
	}
	
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}
	
	public UserDTO getWith() {
		return with;
	}
	
	public void setWith(UserDTO with) {
		this.with = with;
	}
	
	public MessageDTO getLastMessage() {
		return lastMessage;
	}
	
	public void setLastMessage(MessageDTO lastMessage) {
		this.lastMessage = lastMessage;
	}
	
}
