package com.github.ivanjermakov.lettercore.dto;

import com.github.ivanjermakov.lettercore.dto.enums.PreviewType;

public class PreviewDto {

	public PreviewType type;
	public ConversationDto conversation;
	public ChannelDto channel;
	public UserDto with;
	public MessageDto lastMessage;
	public AvatarDto avatar;
	public Integer unread;
	public Boolean kicked;

	public PreviewDto() {
	}

	public PreviewDto(PreviewType type, ConversationDto conversation, ChannelDto channel, UserDto with, MessageDto lastMessage, AvatarDto avatar, Integer unread, Boolean kicked) {
		this.type = type;
		this.conversation = conversation;
		this.channel = channel;
		this.with = with;
		this.lastMessage = lastMessage;
		this.avatar = avatar;
		this.unread = unread;
		this.kicked = kicked;
	}

}
