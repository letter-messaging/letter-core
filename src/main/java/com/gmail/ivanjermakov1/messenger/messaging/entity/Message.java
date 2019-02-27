package com.gmail.ivanjermakov1.messenger.messaging.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.core.util.Objects;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "message")
public class Message {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_id_seq")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "conversation_id")
	private Conversation conversation;
	
	@Column(name = "sent")
	private LocalDateTime sent;
	
	@Column(name = "text")
	private String text;
	
	@Column(name = "read")
	private Boolean read = false;
	
	@OneToOne
	@JoinColumn(name = "sender_id")
	private User sender;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToMany
	@JoinTable(
			name = "forwarded_message",
			joinColumns = @JoinColumn(name = "parent_message_id"),
			inverseJoinColumns = @JoinColumn(name = "forwarded_message_id")
	)
	private List<Message> forwarded;
	
	@OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
	@JoinColumn(name = "message_id", nullable = false)
	private List<Image> images;
	
	public Message() {
	}
	
	public Message(Conversation conversation, LocalDateTime sent, String text, Boolean read, User sender, List<Message> forwarded, List<Image> images) {
		this.conversation = conversation;
		this.sent = sent;
		this.text = text;
		this.read = read;
		this.sender = sender;
		this.forwarded = forwarded;
		this.images = images;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Conversation getConversation() {
		return conversation;
	}
	
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}
	
	public LocalDateTime getSent() {
		return sent;
	}
	
	public void setSent(LocalDateTime sent) {
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
	
	public User getSender() {
		return sender;
	}
	
	public void setSender(User sender) {
		this.sender = sender;
	}
	
	public List<Message> getForwarded() {
		return forwarded;
	}
	
	public void setForwarded(List<Message> forwarded) {
		this.forwarded = forwarded;
	}
	
	public List<Image> getImages() {
		return images;
	}
	
	public void setImages(List<Image> images) {
		this.images = images;
	}
	
	//	TODO: refactor
	public boolean validate() {
		if (sender == null || sender.getId() == null) return false;
		if (conversation == null || conversation.getId() == null) return false;
		
		if (text.trim().isEmpty()) {
			if (Objects.isNullOrEmpty(forwarded) && Objects.isNullOrEmpty(images)) return false;
		}
		
		return true;
	}
	
}
