package com.gmail.ivanjermakov1.messenger.auth.entity;

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
@Table(name = "token")
public class Token {

	/**
	 * Token id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * User, owner of this token
	 */
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * Token value. Represents UUID value
	 */
	@Column(name = "token")
	private String token;
	
	public Token() {
	}
	
	public Token(User user, String token) {
		this.user = user;
		this.token = token;
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
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
}
