package com.gmail.ivanjermakov1.messenger.messaging.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

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
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToMany
	@JoinTable(
			name = "forwarded_message",
			joinColumns = @JoinColumn(name = "parent_message_id"),
			inverseJoinColumns = @JoinColumn(name = "forwarded_message_id")
	)
	private List<Message> forwarded;
	
	public Message() {
	}
	
	public Message(Long conversationId, Instant sent, String text, Boolean read, Long senderId, List<Message> forwarded) {
		this.conversationId = conversationId;
		this.sent = sent;
		this.text = text;
		this.read = read;
		this.senderId = senderId;
		this.forwarded = forwarded;
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
	
	public List<Message> getForwarded() {
		return forwarded;
	}
	
	public void setForwarded(List<Message> forwarded) {
		this.forwarded = forwarded;
	}
	
	public boolean validate() {
		if (conversationId == null) return false;
		return (forwarded != null && !forwarded.isEmpty()) && text != null && !text.isEmpty();
	}
	
}
