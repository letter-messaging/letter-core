package com.gmail.ivanjermakov1.messenger.messaging.dto;

public class NewImageDTO {
	
	private String path;
	
	public NewImageDTO() {
	}
	
	public NewImageDTO(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
}
