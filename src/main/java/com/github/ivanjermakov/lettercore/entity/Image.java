package com.github.ivanjermakov.lettercore.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Entity representing image attached to a certain message
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "image")
public class Image {

	/**
	 * Image id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	/**
	 * Owner user of an image
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User user;

	@ManyToOne
	@JoinColumn(name = "message_id")
	public Message message;

	/**
	 * Static resource path to an image
	 */
	@Column(name = "path")
	public String path;

	/**
	 * Date of an image upload
	 */
	@Column(name = "uploaded")
	public LocalDate uploaded;

	public Image() {
	}

	public Image(User user, Message message, String path, LocalDate uploaded) {
		this.user = user;
		this.message = message;
		this.path = path;
		this.uploaded = uploaded;
	}

}
