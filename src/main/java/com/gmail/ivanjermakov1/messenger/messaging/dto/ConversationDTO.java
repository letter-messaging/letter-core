package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDTO;

import java.util.List;

public class ConversationDTO {
	
	private Long id;
	private Boolean hidden;
	private List<UserDTO> users;
	
	public ConversationDTO() {
	}
	
	public ConversationDTO(Long id, Boolean hidden, List<UserDTO> users) {
		this.id = id;
		this.hidden = hidden;
		this.users = users;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Boolean getHidden() {
		return hidden;
	}
	
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	
	public List<UserDTO> getUsers() {
		return users;
	}
	
	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}
	
}
