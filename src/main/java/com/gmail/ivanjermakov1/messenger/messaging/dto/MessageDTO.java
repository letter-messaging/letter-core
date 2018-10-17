package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;

public class MessageDTO {
	
	private Message message;
	private UserDTO sender;
	private Conversation conversation;
	
	public MessageDTO() {
	}
	
	public MessageDTO(Message message, UserDTO sender, Conversation conversation) {
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
	
	public UserDTO getSender() {
		return sender;
	}
	
	public void setSender(UserDTO sender) {
		this.sender = sender;
	}
	
	public Conversation getConversation() {
		return conversation;
	}
	
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}
	
}
