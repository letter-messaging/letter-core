package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserMainInfo;

public class UserDTO {
	
	private User user;
	private UserMainInfo userMainInfo;
	
	public UserDTO() {
	}
	
	public UserDTO(User user, UserMainInfo userMainInfo) {
		this.user = user;
		this.userMainInfo = userMainInfo;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public UserMainInfo getUserMainInfo() {
		return userMainInfo;
	}
	
	public void setUserMainInfo(UserMainInfo userMainInfo) {
		this.userMainInfo = userMainInfo;
	}
	
}
