package com.github.ivanjermakov.lettercore.messaging.dto.action;

import com.github.ivanjermakov.lettercore.conversation.dto.ConversationDto;
import com.github.ivanjermakov.lettercore.user.dto.UserDto;

public class ConversationReadAction extends Action {

	public ConversationDto conversation;
	public UserDto reader;

	public ConversationReadAction() {
		type = Type.CONVERSATION_READ;
	}

	public ConversationReadAction(ConversationDto conversation, UserDto reader) {
		this();
		this.conversation = conversation;
		this.reader = reader;
	}

}
