package com.gmail.ivanjermakov1.messenger.messaging.entity;

public class Preview {
	
	private Long conversationId;
	
	private Long senderId;
	private String firstName;
	private String lastName;
	
	private Message lastMessage;
	
	public Preview() {
	}
	
	public Preview(Long conversationId, Long senderId, String firstName, String lastName, Message lastMessage) {
		this.conversationId = conversationId;
		this.senderId = senderId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.lastMessage = lastMessage;
	}
	
	public Long getConversationId() {
		return conversationId;
	}
	
	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}
	
	public Long getSenderId() {
		return senderId;
	}
	
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Message getLastMessage() {
		return lastMessage;
	}
	
	public void setLastMessage(Message lastMessage) {
		this.lastMessage = lastMessage;
	}
	
}
