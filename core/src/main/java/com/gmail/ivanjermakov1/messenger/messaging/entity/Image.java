package com.gmail.ivanjermakov1.messenger.messaging.entity;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "image")
public class Image {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "path")
	private String path;
	
	@Column(name = "uploaded")
	private LocalDate uploaded;
	
	public Image() {
	}
	
	public Image(User user, String path, LocalDate uploaded) {
		this.user = user;
		this.path = path;
		this.uploaded = uploaded;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public LocalDate getUploaded() {
		return uploaded;
	}
	
	public void setUploaded(LocalDate uploaded) {
		this.uploaded = uploaded;
	}
	
}
