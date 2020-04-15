package com.github.ivanjermakov.lettercore.file.entity;

import com.github.ivanjermakov.lettercore.user.entity.User;

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
 * Entity representing file attached to a certain message
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "document")
public class Document {

	/**
	 * Document id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	/**
	 * Owner user of a document
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User user;

	/**
	 * Static resource path to a document
	 */
	@Column(name = "path")
	public String path;

	/**
	 * Date of a document upload
	 */
	@Column(name = "uploaded")
	public LocalDate uploaded;

	public Document() {
	}

	public Document(User user, String path, LocalDate uploaded) {
		this.user = user;
		this.path = path;
		this.uploaded = uploaded;
	}

}
