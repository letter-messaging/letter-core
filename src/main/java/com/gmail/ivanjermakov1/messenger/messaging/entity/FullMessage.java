package com.gmail.ivanjermakov1.messenger.messaging.entity;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;

public class FullMessage {
	
	private Message message;
	private User sender;
	private UserMainInfo senderInfo;
	private Conversation conversation;
	
	public FullMessage() {
	}
	
	public FullMessage(Message message, User sender, UserMainInfo senderInfo, Conversation conversation) {
		this.message = message;
		this.sender = sender;
		this.senderInfo = senderInfo;
		this.conversation = conversation;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public void setMessage(Message message) {
		this.message = message;
	}
	
	public User getSender() {
		return sender;
	}
	
	public void setSender(User sender) {
		this.sender = sender;
	}
	
	public UserMainInfo getSenderInfo() {
		return senderInfo;
	}
	
	public void setSenderInfo(UserMainInfo senderInfo) {
		this.senderInfo = senderInfo;
	}
	
	public Conversation getConversation() {
		return conversation;
	}
	
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}
	
}
