package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public class AvatarDto {

	@Value("${default.avatar.chat.path}")
	private String defaultConversationAvatarPath;

	private Long id;
	private String path;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate uploaded;

	public AvatarDto() {
	}

	public AvatarDto(Long id, String path, LocalDate uploaded) {
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
