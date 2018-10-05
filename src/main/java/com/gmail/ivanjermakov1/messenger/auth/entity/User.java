package com.gmail.ivanjermakov1.messenger.auth.entity;

import javax.persistence.*;

@Entity
@Table(name = "user", schema = "public")
public class User {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "login")
	private String login;
	
	@Column(name = "hash")
	private String hash;
	
	public User() {
	}
	
	public User(Long id, String login, String hash) {
		this.id = id;
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
