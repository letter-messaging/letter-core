package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;

import java.util.List;

public class ConversationDto {

	private Long id;
	private String chatName;
	private Boolean hidden;
	private List<UserDto> users;

	public ConversationDto() {
	}

	public ConversationDto(Long id, String chatName, Boolean hidden, List<UserDto> users) {
		this.id = id;
		this.chatName = chatName;
		this.hidden = hidden;
		this.users = users;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChatName() {
		return chatName;
	}

	public void setChatName(String chatName) {
		this.chatName = chatName;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public List<UserDto> getUsers() {
		return users;
	}

	public void setUsers(List<UserDto> users) {
		this.users = users;
	}

}
