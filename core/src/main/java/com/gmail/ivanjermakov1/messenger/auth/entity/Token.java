package com.gmail.ivanjermakov1.messenger.auth.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entity representing user token
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "token")
public class Token {

	/**
	 * Token id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	/**
	 * User, owner of this token
	 */
	@OneToOne
	@JoinColumn(name = "user_id")
	public User user;

	/**
	 * Token value. Represents UUID value
	 */
	@Column(name = "token")
	public String token;

	public Token() {
	}

	public Token(User user, String token) {
		this.user = user;
		this.token = token;
	}

}
