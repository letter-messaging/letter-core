package com.gmail.ivanjermakov1.messenger.messaging.dto.action;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDto;

import static com.gmail.ivanjermakov1.messenger.messaging.dto.action.Action.Type.CONVERSATION_READ;

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

	public ConversationDto getConversation() {
		return conversation;
	}

	public void setConversation(ConversationDto conversation) {
		this.conversation = conversation;
	}

}
