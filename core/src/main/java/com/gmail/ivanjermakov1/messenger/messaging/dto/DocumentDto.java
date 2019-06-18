package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;

import java.time.LocalDate;

public class DocumentDto {
	
	private Long id;
	private UserDto user;
	private String path;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate uploaded;
	
	public DocumentDto() {
	}
	
	public DocumentDto(Long id, UserDto user, String path, LocalDate uploaded) {
		this.id = id;
		this.user = user;
		this.path = path;
		this.uploaded = uploaded;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public UserDto getUser() {
		return user;
	}
	
	public void setUser(UserDto user) {
		this.user = user;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public LocalDate getUploaded() {
		return uploaded;
	}
	
	public void setUploaded(LocalDate uploaded) {
		this.uploaded = uploaded;
	}
	
}
