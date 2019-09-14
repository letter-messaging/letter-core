package com.gmail.ivanjermakov1.messenger.dto.action;

import com.gmail.ivanjermakov1.messenger.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.dto.UserDto;

import static com.gmail.ivanjermakov1.messenger.dto.action.Action.Type.CONVERSATION_READ;

public class ConversationReadAction extends Action {

	public ConversationDto conversation;
	public UserDto reader;

	public ConversationReadAction() {
		type = CONVERSATION_READ;
	}

	public ConversationReadAction(ConversationDto conversation, UserDto reader) {
		this();
		this.conversation = conversation;
		this.reader = reader;
	}

}
