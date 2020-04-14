package com.github.ivanjermakov.lettercore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public class AvatarDto {

	@Value("${default.avatar.chat.path}")
	public String defaultConversationAvatarPath;

	public Long id;
	public String path;

	@JsonFormat(pattern = "yyyy-MM-dd")
	public LocalDate uploaded;

	public AvatarDto() {
	}

	public AvatarDto(Long id, String path, LocalDate uploaded) {
		this.id = id;
		this.path = path;
		this.uploaded = uploaded;
	}

}
