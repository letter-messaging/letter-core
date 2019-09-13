package com.gmail.ivanjermakov1.messenger.messaging.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.core.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing sent message
 */
@Entity
@Table(name = "message")
@Access(AccessType.FIELD)
public class Message {

	/**
	 * Message id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	/**
	 * Conversation message belongs to (was initially sent to)
	 */
	@OneToOne
	@JoinColumn(name = "conversation_id")
	public Conversation conversation;

	/**
	 * Time of message sent
	 */
	@Column(name = "sent")
	public LocalDateTime sent;

	/**
	 * Message's text content
	 */
	@Column(name = "text")
	public String text;

	/**
	 * Message sender
	 */
	@OneToOne
	@JoinColumn(name = "sender_id")
	public User sender;

	/**
	 * List of forwarded messages, attached to current
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToMany
	@JoinTable(
			name = "forwarded_message",
			joinColumns = @JoinColumn(name = "parent_message_id"),
			inverseJoinColumns = @JoinColumn(name = "forwarded_message_id")
	)
	public List<Message> forwarded;

	/**
	 * List of images, attached to current message
	 */
	@OneToMany(mappedBy = "message", cascade = {CascadeType.ALL}, orphanRemoval = true)
	public List<Image> images;

	/**
	 * List of documents, attached to current message
	 */
	@OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
	@JoinColumn(name = "message_id", nullable = false)
	public List<Document> documents;

	public Message() {
	}

	public Message(Conversation conversation, LocalDateTime sent, String text, User sender, List<Message> forwarded, List<Image> images, List<Document> documents) {
		this.conversation = conversation;
		this.sent = sent;
		this.text = text;
		this.sender = sender;
		this.forwarded = forwarded;
		this.images = images;
		this.documents = documents;
	}

	//	TODO: refactor
	public boolean validate() {
		if (sender == null || sender.id == null) return false;
		if (conversation == null || conversation.id == null) return false;

		if (text.trim().isEmpty()) {
			return !Objects.isNullOrEmpty(forwarded) || !Objects.isNullOrEmpty(images) || !Objects.isNullOrEmpty(documents);
		}

		return true;
	}

}
