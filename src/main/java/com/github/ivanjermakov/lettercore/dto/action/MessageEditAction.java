package com.github.ivanjermakov.lettercore.dto.action;

import com.github.ivanjermakov.lettercore.dto.MessageDto;

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
