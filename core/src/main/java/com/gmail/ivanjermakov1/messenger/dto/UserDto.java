package com.gmail.ivanjermakov1.messenger.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class UserDto {

	public Long id;
	public String login;
	public String firstName;
	public String lastName;
	public AvatarDto avatar;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public LocalDateTime lastSeen;

	public UserDto() {
	}

	public UserDto(Long id, String login, String firstName, String lastName, AvatarDto avatar, LocalDateTime lastSeen) {
		this.id = id;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatar = avatar;
		this.lastSeen = lastSeen;
	}

}
