package com.github.ivanjermakov.lettercore.file.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.ivanjermakov.lettercore.user.dto.UserDto;

import java.time.LocalDate;

public class DocumentDto {

	public Long id;
	public UserDto user;
	public String path;

	@JsonFormat(pattern = "yyyy-MM-dd")
	public LocalDate uploaded;

	public DocumentDto() {
	}

	public DocumentDto(Long id, UserDto user, String path, LocalDate uploaded) {
		this.id = id;
		this.user = user;
		this.path = path;
		this.uploaded = uploaded;
	}

}
