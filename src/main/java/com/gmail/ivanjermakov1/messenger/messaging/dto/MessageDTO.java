package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmail.ivanjermakov1.messenger.auth.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

public class MessageDTO {
	
	private Long id;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime sent;
	private String text;
	private UserDTO sender;
	private ConversationDTO conversation;
	private List<MessageDTO> forwarded;
	
	public MessageDTO() {
	}
	
	public MessageDTO(Long id, LocalDateTime sent, String text, UserDTO sender, ConversationDTO conversation, List<MessageDTO> forwarded) {
		this.id = id;
		this.sent = sent;
		this.text = text;
		this.sender = sender;
		this.conversation = conversation;
		this.forwarded = forwarded;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDateTime getSent() {
		return sent;
	}
	
	public void setSent(LocalDateTime sent) {
		this.sent = sent;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public UserDTO getSender() {
		return sender;
	}
	
	public void setSender(UserDTO sender) {
		this.sender = sender;
	}
	
	public ConversationDTO getConversation() {
		return conversation;
	}
	
	public void setConversation(ConversationDTO conversation) {
		this.conversation = conversation;
	}
	
	public List<MessageDTO> getForwarded() {
		return forwarded;
	}
	
	public void setForwarded(List<MessageDTO> forwarded) {
		this.forwarded = forwarded;
	}
	
}
