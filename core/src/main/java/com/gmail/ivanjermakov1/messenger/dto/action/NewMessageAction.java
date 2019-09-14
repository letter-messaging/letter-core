package com.gmail.ivanjermakov1.messenger.dto.action;

import com.gmail.ivanjermakov1.messenger.dto.MessageDto;

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
