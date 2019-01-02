package com.gmail.ivanjermakov1.messenger.messaging.dto.action;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDTO;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ConversationDTO;

import static com.gmail.ivanjermakov1.messenger.messaging.dto.action.Action.Type.CONVERSATION_READ;

public class ConversationReadAction extends Action {
	
	private ConversationDTO conversation;
	private UserDTO reader;
	
	public ConversationReadAction() {
		setType(CONVERSATION_READ);
	}
	
	public ConversationReadAction(ConversationDTO conversation, UserDTO reader) {
		this.conversation = conversation;
		this.reader = reader;
	}
	
	public ConversationDTO getConversation() {
		return conversation;
	}
	
	public void setConversation(ConversationDTO conversation) {
		this.conversation = conversation;
	}
	
	public UserDTO getReader() {
		return reader;
	}
	
	public void setReader(UserDTO reader) {
		this.reader = reader;
	}
	
}
