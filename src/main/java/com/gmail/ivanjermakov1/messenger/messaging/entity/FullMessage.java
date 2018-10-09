package com.gmail.ivanjermakov1.messenger.messaging.entity;

public class FullMessage {
	
	private Message message;
	private FullUser sender;
	private Conversation conversation;
	
	public FullMessage() {
	}
	
	public FullMessage(Message message, FullUser sender, Conversation conversation) {
		this.message = message;
		this.sender = sender;
		this.conversation = conversation;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public void setMessage(Message message) {
		this.message = message;
	}
	
	public FullUser getSender() {
		return sender;
	}
	
	public void setSender(FullUser sender) {
		this.sender = sender;
	}
	
	public Conversation getConversation() {
		return conversation;
	}
	
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}
	
}
