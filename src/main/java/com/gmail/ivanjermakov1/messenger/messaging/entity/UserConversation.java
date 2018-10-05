package com.gmail.ivanjermakov1.messenger.messaging.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_conversation")
public class UserConversation {
	
	@Id
	@JoinColumn(name = "user_id",
			foreignKey = @ForeignKey(name = "user_conversation_user_id_fk")
	)
	private Long userId;
	
	@JoinColumn(name = "conversation_id",
			foreignKey = @ForeignKey(name = "user_conversation_conversation_id_fk")
	)
	private Long conversationId;
	
	public UserConversation() {
	}
	
	public UserConversation(Long userId, Long conversationId) {
		this.userId = userId;
		this.conversationId = conversationId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getConversationId() {
		return conversationId;
	}
	
	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}
	
}
