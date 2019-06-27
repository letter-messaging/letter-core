package com.gmail.ivanjermakov1.messenger.messaging.entity;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "conversation")
public class Conversation {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "chat_name")
	private String chatName;
	
	@OneToMany(mappedBy = "conversation", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private List<UserConversation> userConversations;
	
	@ManyToOne
	@JoinColumn(name = "creator")
	private User creator;
	
	public Conversation() {
	}
	
	public Conversation(String chatName, List<UserConversation> userConversations, User creator) {
		this.chatName = chatName;
		this.userConversations = userConversations;
		this.creator = creator;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getChatName() {
		return chatName;
	}
	
	public void setChatName(String chatName) {
		this.chatName = chatName;
	}
	
	public List<UserConversation> getUserConversations() {
		return userConversations;
	}
	
	public void setUserConversations(List<UserConversation> userConversations) {
		this.userConversations = userConversations;
	}
	
	public User getCreator() {
		return creator;
	}
	
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
}
