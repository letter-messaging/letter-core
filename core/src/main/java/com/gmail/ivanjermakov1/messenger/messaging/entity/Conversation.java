package com.gmail.ivanjermakov1.messenger.messaging.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
	@OneToMany(mappedBy = "conversation", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private List<UserConversation> userConversations;
	
	public Conversation() {
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public List<UserConversation> getUserConversations() {
		return userConversations;
	}
	
	public void setUserConversations(List<UserConversation> userConversations) {
		this.userConversations = userConversations;
	}
	
}
