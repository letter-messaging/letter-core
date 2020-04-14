package com.gmail.ivanjermakov1.messenger.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class ImageDto {

	public Long id;
	public UserDto user;
	public String path;

	@JsonFormat(pattern = "yyyy-MM-dd")
	public LocalDate uploaded;

	public ImageDto() {
	}

	public ImageDto(Long id, UserDto user, String path, LocalDate uploaded) {
		this.id = id;
		this.user = user;
		this.path = path;
		this.uploaded = uploaded;
	}

}
