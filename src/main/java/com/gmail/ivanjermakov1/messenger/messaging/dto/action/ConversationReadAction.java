package com.gmail.ivanjermakov1.messenger.messaging.dto.action;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDTO;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;

import static com.gmail.ivanjermakov1.messenger.messaging.dto.action.Action.Type.CONVERSATION_READ;

public class ConversationReadAction extends Action {
	
	private Conversation conversation;
	private UserDTO reader;
	
	public ConversationReadAction() {
		setType(CONVERSATION_READ);
	}
	
	public ConversationReadAction(Conversation conversation, UserDTO reader) {
		this();
		this.conversation = conversation;
		this.reader = reader;
	}
	
	public Conversation getConversation() {
		return conversation;
	}
	
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}
	
	public UserDTO getReader() {
		return reader;
	}
	
	public void setReader(UserDTO reader) {
		this.reader = reader;
	}
	
}
