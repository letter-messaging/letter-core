package com.gmail.ivanjermakov1.messenger.auth.dto;

public class UserDTO {
	
	private Long id;
	private String login;
	private String firstName;
	private String lastName;
	
	public UserDTO() {
	}
	
	public UserDTO(Long id, String login, String firstName, String lastName) {
		this.id = id;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
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
	
}
