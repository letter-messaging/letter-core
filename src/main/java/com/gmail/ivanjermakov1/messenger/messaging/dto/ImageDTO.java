package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmail.ivanjermakov1.messenger.auth.dto.UserDTO;

import java.time.LocalDate;

public class ImageDTO {
	
	private Long id;
	private UserDTO user;
	private String path;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate uploaded;
	
	public ImageDTO() {
	}
	
	public ImageDTO(Long id, UserDTO user, String path, LocalDate uploaded) {
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
	
	public UserDTO getUser() {
		return user;
	}
	
	public void setUser(UserDTO user) {
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
