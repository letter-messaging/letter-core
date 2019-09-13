package com.gmail.ivanjermakov1.messenger.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gmail.ivanjermakov1.messenger.auth.service.HashService;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity representing user
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "user", schema = "public")
public class User {

	/**
	 * User id. System account has is {@code 0}
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	/**
	 * User login. System account has login {@code @system}
	 */
	@Column(name = "login")
	public String login;

	/**
	 * Hashed version of user password. Hashed using {@link HashService}
	 *
	 * @see HashService
	 */
	@JsonIgnore
	@Column(name = "hash")
	public String hash;

	public User() {
	}

	public User(String login, String hash) {
		this.login = login;
		this.hash = hash;
	}

}
