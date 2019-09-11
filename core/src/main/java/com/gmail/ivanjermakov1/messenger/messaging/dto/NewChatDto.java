package com.gmail.ivanjermakov1.messenger.messaging.dto;

import java.util.List;

public class NewChatDto {

	private String name;
	private List<Long> userIds;

	public NewChatDto() {
	}

	public NewChatDto(String name, List<Long> userIds) {
		this.name = name;
		this.userIds = userIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

}
