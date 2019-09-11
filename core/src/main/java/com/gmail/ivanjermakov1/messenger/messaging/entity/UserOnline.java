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
 * Entity represents info about user online status
 */
@Entity
@Table(name = "user_online")
public class UserOnline {

	/**
	 * User online id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * User itself
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * Timestamp of last user's presence
	 */
	@Column(name = "seen")
	private LocalDateTime seen;

	public UserOnline() {
	}

	public UserOnline(User user, LocalDateTime seen) {
		this.user = user;
		this.seen = seen;
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

	public LocalDateTime getSeen() {
		return seen;
	}

	public void setSeen(LocalDateTime seen) {
		this.seen = seen;
	}

}
