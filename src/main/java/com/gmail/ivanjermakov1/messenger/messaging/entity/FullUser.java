package com.gmail.ivanjermakov1.messenger.messaging.entity;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;

public class FullUser {
	
	private User user;
	private UserMainInfo userMainInfo;
	
	public FullUser() {
	}
	
	public FullUser(User user, UserMainInfo userMainInfo) {
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
