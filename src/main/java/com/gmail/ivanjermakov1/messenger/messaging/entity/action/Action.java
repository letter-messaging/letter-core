package com.gmail.ivanjermakov1.messenger.messaging.entity.action;

public abstract class Action {
	
	public enum Type {
		NEW_MESSAGE,
		MESSAGE_EDIT,
		CONVERSATION_READ,
		USER_ONLINE
	}
	
	private Type type;
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
}
