package com.gmail.ivanjermakov1.messenger.messaging.entity;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Entity representing user state in certain conversation
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "user_conversation")
public class UserConversation {

	/**
	 * User conversation id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	/**
	 * User itself
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User user;

	/**
	 * Conversation related to user
	 */
	@ManyToOne
	@JoinColumn(name = "conversation_id")
	public Conversation conversation;

	/**
	 * Whether conversation is hidden for this user
	 */
	@Column(name = "hidden")
	public Boolean hidden;

	/**
	 * Timestamp of last conversation read by this user
	 */
	@Column(name = "last_read")
	public LocalDateTime lastRead;

	/**
	 * Whether user is kicked from this conversation. Make sense only when conversation has type of CHAT
	 */
	@Column(name = "kicked")
	public Boolean kicked;

	public UserConversation() {
	}

	public UserConversation(User user, Conversation conversation) {
		this.user = user;
		this.conversation = conversation;
		this.hidden = false;
		this.lastRead = LocalDateTime.now();
		this.kicked = false;
	}

}
