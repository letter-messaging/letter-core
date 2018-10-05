package com.gmail.ivanjermakov1.messenger.messaging.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "message")
public class Message {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JoinColumn(name = "conversation_id",
			foreignKey = @ForeignKey(name = "token_user_id_fk")
	)
	private Long conversationId;
	
	@Column(name = "sent")
	private Instant sent;
	
	@Column(name = "text")
	private String text;
	
	@Column(name = "read")
	private Boolean read = false;
	
	@Column(name = "sender_id")
	private Long senderId;
	
	public Message() {
	}
	
	public Message(Long id, Long conversationId, Instant sent, String text, Boolean read, Long senderId) {
		this.id = id;
		this.conversationId = conversationId;
		this.sent = sent;
		this.text = text;
		this.read = read;
		this.senderId = senderId;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getConversationId() {
		return conversationId;
	}
	
	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}
	
	public Instant getSent() {
		return sent;
	}
	
	public void setSent(Instant sent) {
		this.sent = sent;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Boolean getRead() {
		return read;
	}
	
	public void setRead(Boolean read) {
		this.read = read;
	}
	
	public Long getSenderId() {
		return senderId;
	}
	
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	
}
