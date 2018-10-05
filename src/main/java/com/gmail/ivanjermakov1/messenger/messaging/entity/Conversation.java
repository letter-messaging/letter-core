package com.gmail.ivanjermakov1.messenger.messaging.entity;

import javax.persistence.*;

@Entity
@Table(name = "conversation")
public class Conversation {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public Conversation() {
	}
	
	public Conversation(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
}
