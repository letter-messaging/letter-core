package com.gmail.ivanjermakov1.messenger.messaging.entity.action;

import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDTO;

import static com.gmail.ivanjermakov1.messenger.messaging.entity.action.Action.Type.NEW_MESSAGE;

public class NewMessage extends Action {
	
	private MessageDTO message;
	
	public NewMessage() {
		setType(NEW_MESSAGE);
	}
	
	public NewMessage(MessageDTO message) {
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
