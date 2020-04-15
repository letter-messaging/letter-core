package com.github.ivanjermakov.lettercore.messaging.dto.action;

import com.github.ivanjermakov.lettercore.messaging.dto.MessageDto;

public class MessageEditAction extends Action {

	public MessageDto message;

	public MessageEditAction() {
		type = Type.MESSAGE_EDIT;
	}

	public MessageEditAction(MessageDto message) {
		this();
		this.message = message;
	}

}
