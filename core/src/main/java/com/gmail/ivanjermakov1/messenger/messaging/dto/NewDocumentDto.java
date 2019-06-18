package com.gmail.ivanjermakov1.messenger.messaging.dto;

public class NewDocumentDto {
	
	private String path;
	
	public NewDocumentDto() {
	}
	
	public NewDocumentDto(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
}
