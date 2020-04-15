package com.github.ivanjermakov.lettercore.dto;

public class ChannelDto {

	public Long id;
	public String name;
	public Boolean hidden;

	public ChannelDto() {
	}

	public ChannelDto(Long id, String name, Boolean hidden) {
		this.id = id;
		this.name = name;
		this.hidden = hidden;
	}

}
