package com.gmail.ivanjermakov1.messenger.messaging.dto.action;

import com.gmail.ivanjermakov1.messenger.messaging.dto.MessageDto;

public class NewMessageAction extends Action {

	private MessageDto message;

	public NewMessageAction() {
		setType(Type.NEW_MESSAGE);
	}

	public NewMessageAction(MessageDto message) {
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
