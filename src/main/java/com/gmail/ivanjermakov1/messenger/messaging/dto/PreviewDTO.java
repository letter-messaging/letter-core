package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDTO;

public class PreviewDTO {
	
	private ConversationDTO conversation;
	private UserDTO with;
	private MessageDTO lastMessage;
	private Integer unread;
	
	public PreviewDTO() {
	}
	
	public PreviewDTO(ConversationDTO conversation, UserDTO with, MessageDTO lastMessage, Integer unread) {
		this.conversation = conversation;
		this.with = with;
		this.lastMessage = lastMessage;
		this.unread = unread;
	}
	
	public ConversationDTO getConversation() {
		return conversation;
	}
	
	public void setConversation(ConversationDTO conversation) {
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
	
	public Integer getUnread() {
		return unread;
	}
	
	public void setUnread(Integer unread) {
		this.unread = unread;
	}
	
}
