package com.gmail.ivanjermakov1.messenger.messaging.dto;

public class ChannelDto {
	
	private Long id;
	private String name;
	private Boolean hidden;
	
	public ChannelDto() {
	}
	
	public ChannelDto(Long id, String name, Boolean hidden) {
		this.id = id;
		this.name = name;
		this.hidden = hidden;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getHidden() {
		return hidden;
	}
	
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	
}
