package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;

public class PreviewDto {
	
	private ConversationDto conversation;
	private UserDto with;
	private MessageDto lastMessage;
	private Integer unread;
	
	public PreviewDto() {
	}
	
	public PreviewDto(ConversationDto conversation, UserDto with, MessageDto lastMessage, Integer unread) {
		this.conversation = conversation;
		this.with = with;
		this.lastMessage = lastMessage;
		this.unread = unread;
	}
	
	public ConversationDto getConversation() {
		return conversation;
	}
	
	public void setConversation(ConversationDto conversation) {
		this.conversation = conversation;
	}
	
	public UserDto getWith() {
		return with;
	}
	
	public void setWith(UserDto with) {
		this.with = with;
	}
	
	public MessageDto getLastMessage() {
		return lastMessage;
	}
	
	public void setLastMessage(MessageDto lastMessage) {
		this.lastMessage = lastMessage;
	}
	
	public Integer getUnread() {
		return unread;
	}
	
	public void setUnread(Integer unread) {
		this.unread = unread;
	}
	
}
