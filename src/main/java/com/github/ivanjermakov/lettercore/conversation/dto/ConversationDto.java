package com.github.ivanjermakov.lettercore.conversation.dto;

import com.github.ivanjermakov.lettercore.user.dto.UserDto;

import java.util.List;

public class ConversationDto {

	public Long id;
	public String chatName;
	public Boolean hidden;
	public List<UserDto> users;

	public ConversationDto() {
	}

	public ConversationDto(Long id, String chatName, Boolean hidden, List<UserDto> users) {
		this.id = id;
		this.chatName = chatName;
		this.hidden = hidden;
		this.users = users;
	}

}
