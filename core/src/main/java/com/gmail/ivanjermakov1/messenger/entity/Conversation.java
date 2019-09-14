package com.gmail.ivanjermakov1.messenger.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Entity representing conversation
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "conversation")
public class Conversation {

	/**
	 * Conversation id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	/**
	 * If conversation is chat then this field represents its name. Otherwise field is {@code null}
	 */
	@Column(name = "chat_name")
	public String chatName;

	/**
	 * List of intermediate table entities user conversations
	 */
	@OneToMany(mappedBy = "conversation", cascade = {CascadeType.ALL}, orphanRemoval = true)
	public List<UserConversation> userConversations;

	/**
	 * Creator user of conversation
	 */
	@ManyToOne
	@JoinColumn(name = "creator_id")
	public User creator;

	public Conversation() {
	}

	public Conversation(String chatName, List<UserConversation> userConversations, User creator) {
		this.chatName = chatName;
		this.userConversations = userConversations;
		this.creator = creator;
	}

}
