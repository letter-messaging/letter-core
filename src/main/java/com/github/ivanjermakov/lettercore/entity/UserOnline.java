package com.github.ivanjermakov.lettercore.entity;

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
 * Entity represents info about user online status
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "user_online")
public class UserOnline {

	/**
	 * User online id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	/**
	 * User itself
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User user;

	/**
	 * Timestamp of last user's presence
	 */
	@Column(name = "seen")
	public LocalDateTime seen;

	public UserOnline() {
	}

	public UserOnline(User user, LocalDateTime seen) {
		this.user = user;
		this.seen = seen;
	}

}
