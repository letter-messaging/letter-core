package com.gmail.ivanjermakov1.messenger.dto.action;

import com.gmail.ivanjermakov1.messenger.dto.MessageDto;

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
