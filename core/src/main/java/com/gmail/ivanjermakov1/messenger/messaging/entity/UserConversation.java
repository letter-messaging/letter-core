package com.gmail.ivanjermakov1.messenger.messaging.entity;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;

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
@Table(name = "user_conversation")
public class UserConversation {

	/**
	 * User conversation id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * User itself
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * Conversation related to user
	 */
	@ManyToOne
	@JoinColumn(name = "conversation_id")
	private Conversation conversation;

	/**
	 * Whether conversation is hidden for this user
	 */
	@Column(name = "hidden")
	private Boolean hidden;

	/**
	 * Timestamp of last conversation read by this user
	 */
	@Column(name = "last_read")
	private LocalDateTime lastRead;

	/**
	 * Whether user is kicked from this conversation. Make sense only when conversation has type of CHAT
	 */
	@Column(name = "kicked")
	private Boolean kicked;

	public UserConversation() {
	}

	public UserConversation(User user, Conversation conversation) {
		this.user = user;
		this.conversation = conversation;
		this.hidden = false;
		this.lastRead = LocalDateTime.now();
		this.kicked = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public LocalDateTime getLastRead() {
		return lastRead;
	}

	public void setLastRead(LocalDateTime lastRead) {
		this.lastRead = lastRead;
	}

	public Boolean getKicked() {
		return kicked;
	}

	public void setKicked(Boolean kicked) {
		this.kicked = kicked;
	}

}
