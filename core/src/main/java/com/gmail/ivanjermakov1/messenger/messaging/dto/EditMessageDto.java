package com.gmail.ivanjermakov1.messenger.messaging.dto;

import java.util.List;

public class EditMessageDto {

	public Long id;
	public String text;
	public List<MessageDto> forwarded;
	public List<ImageDto> images;
	public List<DocumentDto> documents;

	public EditMessageDto() {
	}

	public EditMessageDto(Long id, String text, List<MessageDto> forwarded, List<ImageDto> images, List<DocumentDto> documents) {
		this.id = id;
		this.text = text;
		this.forwarded = forwarded;
		this.images = images;
		this.documents = documents;
	}

}
