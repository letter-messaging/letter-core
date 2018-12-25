package com.gmail.ivanjermakov1.messenger.messaging.dto.action;

import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDTO;

public class MessageEditAction extends Action {
	
	private MessageDTO message;
	
	public MessageEditAction() {
		setType(Type.MESSAGE_EDIT);
	}
	
	public MessageEditAction(MessageDTO message) {
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
