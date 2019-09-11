package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.enums.PreviewType;

public class PreviewDto {

	private PreviewType type;
	private ConversationDto conversation;
	private ChannelDto channel;
	private UserDto with;
	private MessageDto lastMessage;
	private AvatarDto avatar;
	private Integer unread;
	private Boolean kicked;

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

	public PreviewType getType() {
		return type;
	}

	public void setType(PreviewType type) {
		this.type = type;
	}

	public ConversationDto getConversation() {
		return conversation;
	}

	public void setConversation(ConversationDto conversation) {
		this.conversation = conversation;
	}

	public ChannelDto getChannel() {
		return channel;
	}

	public void setChannel(ChannelDto channel) {
		this.channel = channel;
	}

	public UserDto getWith() {
		return with;
	}

	public void setWith(UserDto with) {
		this.with = with;
	}

	public MessageDto getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(MessageDto lastMessage) {
		this.lastMessage = lastMessage;
	}

	public AvatarDto getAvatar() {
		return avatar;
	}

	public void setAvatar(AvatarDto avatar) {
		this.avatar = avatar;
	}

	public Integer getUnread() {
		return unread;
	}

	public void setUnread(Integer unread) {
		this.unread = unread;
	}

	public Boolean getKicked() {
		return kicked;
	}

	public void setKicked(Boolean kicked) {
		this.kicked = kicked;
	}

}
