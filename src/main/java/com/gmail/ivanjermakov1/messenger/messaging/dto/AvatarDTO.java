package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class AvatarDTO {
	
	private Long id;
	private String path;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate uploaded;
	
	public AvatarDTO() {
	}
	
	public AvatarDTO(Long id, String path, LocalDate uploaded) {
		this.id = id;
		this.path = path;
		this.uploaded = uploaded;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
