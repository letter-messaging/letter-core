package com.github.ivanjermakov.lettercore.messaging.dto.action;

public abstract class Action {

	public enum Type {
		NEW_MESSAGE,
		MESSAGE_EDIT,
		CONVERSATION_READ,
		USER_ONLINE
	}

	public Type type;

}
