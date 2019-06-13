package com.gmail.ivanjermakov1.messenger.messaging.dto;

import java.util.List;

public class EditMessageDto {
	
	private Long id;
	private String text;
	private List<MessageDto> forwarded;
	private List<ImageDto> images;
	
	public EditMessageDto() {
	}
	
	public EditMessageDto(Long id, String text, List<MessageDto> forwarded, List<ImageDto> images) {
		this.id = id;
		this.text = text;
		this.forwarded = forwarded;
		this.images = images;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public List<MessageDto> getForwarded() {
		return forwarded;
	}
	
	public void setForwarded(List<MessageDto> forwarded) {
		this.forwarded = forwarded;
	}
	
	public List<ImageDto> getImages() {
		return images;
	}
	
	public void setImages(List<ImageDto> images) {
		this.images = images;
	}
	
}
