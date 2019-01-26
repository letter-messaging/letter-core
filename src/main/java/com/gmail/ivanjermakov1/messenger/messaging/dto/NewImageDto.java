package com.gmail.ivanjermakov1.messenger.messaging.dto;

public class NewImageDto {
	
	private String path;
	
	public NewImageDto() {
	}
	
	public NewImageDto(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
}
