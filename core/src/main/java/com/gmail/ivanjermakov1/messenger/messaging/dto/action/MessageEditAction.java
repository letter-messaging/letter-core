package com.gmail.ivanjermakov1.messenger.messaging.dto.action;

import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDto;

public class MessageEditAction extends Action {
	
	private MessageDto message;
	
	public MessageEditAction() {
		setType(Type.MESSAGE_EDIT);
	}
	
	public MessageEditAction(MessageDto message) {
		this();
		this.message = message;
	}
	
	public MessageDto getMessage() {
		return message;
	}
	
	public void setMessage(MessageDto message) {
		this.message = message;
	}
	
}
