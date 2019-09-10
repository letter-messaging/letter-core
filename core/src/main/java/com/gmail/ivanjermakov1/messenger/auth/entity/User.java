package com.gmail.ivanjermakov1.messenger.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "user", schema = "public")
public class User {

	/**
	 * User id. System account has is {@code 0}
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * User login. System account has login {@code @system}
	 */
	@Column(name = "login")
	private String login;

	/**
	 * Hashed version of user password. Hashed using {@link com.gmail.ivanjermakov1.messenger.auth.security.Hasher}
	 * @see com.gmail.ivanjermakov1.messenger.auth.security.Hasher
	 */
	@JsonIgnore
	@Column(name = "hash")
	private String hash;
	
	public User() {
	}
	
	public User(String login, String hash) {
		this.login = login;
		this.hash = hash;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getHash() {
		return hash;
	}
	
	public void setHash(String hash) {
		this.hash = hash;
	}
	
}
