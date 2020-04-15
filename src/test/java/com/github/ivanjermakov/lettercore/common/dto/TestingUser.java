package com.github.ivanjermakov.lettercore.common.dto;

import com.github.ivanjermakov.lettercore.user.dto.UserDto;
import com.github.ivanjermakov.lettercore.user.entity.User;

public class TestingUser {

	public User user;
	public UserDto userDto;
	public String token;

	public TestingUser() {
	}

	public TestingUser(User user, UserDto userDto, String token) {
		this.user = user;
		this.userDto = userDto;
		this.token = token;
	}

}
