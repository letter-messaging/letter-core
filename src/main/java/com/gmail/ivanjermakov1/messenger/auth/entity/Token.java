package com.gmail.ivanjermakov1.messenger.auth.entity;

import javax.persistence.*;

@Entity
@Table(name = "token")
public class Token {
	
	@Id
	@JoinColumn(name = "id")
	private Long id;
	
	@Column(name = "token")
	private String token;
	
	public Token() {
	}
	
	public Token(Long id, String token) {
		this.id = id;
		this.token = token;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
}
