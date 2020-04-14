package com.github.ivanjermakov.lettercore.entity;

import com.github.ivanjermakov.lettercore.dto.enums.MaritalStatus;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Entity represents user info
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "user_info")
public class UserInfo {

	/**
	 * User info id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	/**
	 * User itself
	 */
	@OneToOne
	@JoinColumn(name = "user_id")
	public User user;

	/**
	 * User first name
	 */
	@Column(name = "first_name")
	public String firstName;

	/**
	 * User last name
	 */
	@Column(name = "last_name")
	public String lastName;

	/**
	 * User gender. True is male, False is female
	 */
	@Column(name = "gender")
	public Boolean gender;

	/**
	 * User birth date
	 */
	@Column(name = "birth_date")
	public LocalDate birthDate;

	/**
	 * User marital status
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "marital_status")
	public MaritalStatus maritalStatus;

	/**
	 * User living country
	 */
	@Column(name = "country")
	public String country;

	/**
	 * User living city
	 */
	@Column(name = "city")
	public String city;

	/**
	 * User living location
	 */
	@Column(name = "location")
	public String location;

	/**
	 * User phone number in {@link String} format
	 */
	@Column(name = "phone_number")
	public String phoneNumber;

	/**
	 * User mail in {@link String} format
	 */
	@Column(name = "mail")
	public String mail;

	/**
	 * User place of education
	 */
	@Column(name = "place_of_education")
	public String placeOfEducation;

	/**
	 * User place of work
	 */
	@Column(name = "place_of_work")
	public String placeOfWork;

	/**
	 * User about info
	 */
	@Column(name = "about")
	public String about;

	public UserInfo() {
	}

	public UserInfo(User user, String firstName, String lastName) {
		this.user = user;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public UserInfo(User user, String firstName, String lastName, Boolean gender, LocalDate birthDate, MaritalStatus maritalStatus, String country, String city, String location, String phoneNumber, String mail, String placeOfEducation, String placeOfWork, String about) {
		this.user = user;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthDate = birthDate;
		this.maritalStatus = maritalStatus;
		this.country = country;
		this.city = city;
		this.location = location;
		this.phoneNumber = phoneNumber;
		this.mail = mail;
		this.placeOfEducation = placeOfEducation;
		this.placeOfWork = placeOfWork;
		this.about = about;
	}

}
