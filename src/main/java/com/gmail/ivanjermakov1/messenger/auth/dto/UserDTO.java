package com.gmail.ivanjermakov1.messenger.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class UserDTO {
	
	private Long id;
	private String login;
	private String firstName;
	private String lastName;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastSeen;
	
	public UserDTO() {
	}
	
	public UserDTO(Long id, String login, String firstName, String lastName, LocalDateTime lastSeen) {
		this.id = id;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.lastSeen = lastSeen;
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
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public LocalDateTime getLastSeen() {
		return lastSeen;
	}
	
	public void setLastSeen(LocalDateTime lastSeen) {
		this.lastSeen = lastSeen;
	}
	
}
