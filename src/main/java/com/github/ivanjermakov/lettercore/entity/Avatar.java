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
 * Entity representing image file of user profile picture
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "avatar")
public class Avatar {

	/**
	 * Avatar id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	/**
	 * Avatar owner user
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User user;

	/**
	 * Static resource path to avatar
	 */
	@Column(name = "path")
	public String path;

	/**
	 * Date of avatar upload
	 */
	@Column(name = "uploaded")
	public LocalDate uploaded;

	public Avatar() {
	}

	public Avatar(User user, String path, LocalDate uploaded) {
		this.user = user;
		this.path = path;
		this.uploaded = uploaded;
	}

}
