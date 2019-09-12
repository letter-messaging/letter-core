package com.gmail.ivanjermakov1.messenger.messaging.entity;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.dto.enums.MaritalStatus;

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
@Table(name = "user_info")
public class UserInfo {

	/**
	 * User info id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * User itself
	 */
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * User first name
	 */
	@Column(name = "first_name")
	private String firstName;

	/**
	 * User last name
	 */
	@Column(name = "last_name")
	private String lastName;

	/**
	 * User gender. True is male, False is female
	 */
	@Column(name = "gender")
	private Boolean gender;

	/**
	 * User birth date
	 */
	@Column(name = "birth_date")
	private LocalDate birthDate;

	/**
	 * User marital status
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "marital_status")
	private MaritalStatus maritalStatus;

	/**
	 * User living country
	 */
	@Column(name = "country")
	private String country;

	/**
	 * User living city
	 */
	@Column(name = "city")
	private String city;

	/**
	 * User living location
	 */
	@Column(name = "location")
	private String location;

	/**
	 * User phone number in {@link String} format
	 */
	@Column(name = "phone_number")
	private String phoneNumber;

	/**
	 * User mail in {@link String} format
	 */
	@Column(name = "mail")
	private String mail;

	/**
	 * User place of education
	 */
	@Column(name = "place_of_education")
	private String placeOfEducation;

	/**
	 * User place of work
	 */
	@Column(name = "place_of_work")
	private String placeOfWork;

	/**
	 * User about info
	 */
	@Column(name = "about")
	private String about;

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPlaceOfEducation() {
		return placeOfEducation;
	}

	public void setPlaceOfEducation(String placeOfEducation) {
		this.placeOfEducation = placeOfEducation;
	}

	public String getPlaceOfWork() {
		return placeOfWork;
	}

	public void setPlaceOfWork(String placeOfWork) {
		this.placeOfWork = placeOfWork;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

}
