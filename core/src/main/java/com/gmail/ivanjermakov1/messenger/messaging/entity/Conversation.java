package com.gmail.ivanjermakov1.messenger.messaging.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_conversation",
			joinColumns = @JoinColumn(name = "conversation_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private List<User> users;
	
	@OneToMany(mappedBy = "conversation")
	private List<UserConversation> userConversations;
	
	public Conversation() {
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public List<UserConversation> getUserConversations() {
		return userConversations;
	}
	
	public void setUserConversations(List<UserConversation> userConversations) {
		this.userConversations = userConversations;
	}
	
}
