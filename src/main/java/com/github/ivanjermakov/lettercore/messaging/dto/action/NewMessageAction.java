package com.github.ivanjermakov.lettercore.messaging.dto.action;

import com.github.ivanjermakov.lettercore.messaging.dto.MessageDto;

public class NewMessageAction extends Action {

	public MessageDto message;

	public NewMessageAction() {
		type = Type.NEW_MESSAGE;
	}

	public NewMessageAction(MessageDto message) {
		this();
		this.message = message;
	}

}
