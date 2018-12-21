package com.gmail.ivanjermakov1.messenger.messaging.dto.action;

import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDTO;

public class NewMessageAction extends Action {
	
	private MessageDTO message;
	
	public NewMessageAction() {
		setType(Type.NEW_MESSAGE);
	}
	
	public NewMessageAction(MessageDTO message) {
		this();
		this.message = message;
	}
	
	public MessageDTO getMessage() {
		return message;
	}
	
	public void setMessage(MessageDTO message) {
		this.message = message;
	}
	
}
