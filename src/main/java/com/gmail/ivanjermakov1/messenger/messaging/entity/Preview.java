package com.gmail.ivanjermakov1.messenger.messaging.entity;

public class Preview {
	
	private Conversation conversation;
	private FullUser with;
	private FullMessage lastMessage;
	
	public Preview() {
	}
	
	public Preview(Conversation conversation, FullUser with, FullMessage lastMessage) {
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
	
	public FullUser getWith() {
		return with;
	}
	
	public void setWith(FullUser with) {
		this.with = with;
	}
	
	public FullMessage getLastMessage() {
		return lastMessage;
	}
	
	public void setLastMessage(FullMessage lastMessage) {
		this.lastMessage = lastMessage;
	}
	
}
