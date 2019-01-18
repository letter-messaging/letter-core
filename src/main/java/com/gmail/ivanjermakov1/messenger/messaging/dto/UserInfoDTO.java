package com.gmail.ivanjermakov1.messenger.messaging.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserInfo;

import java.time.LocalDate;

public class UserInfoDTO {
	
	private User user;
	private String firstName;
	private String lastName;
	private Boolean gender;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	private MaritalStatus maritalStatus;
	private String country;
	private String city;
	private String location;
	private String phoneNumber;
	private String mail;
	private String placeOfEducation;
	private String placeOfWork;
	private String about;
	
	public UserInfoDTO() {
	}
	
	public UserInfoDTO(User user, String firstName, String lastName, Boolean gender, LocalDate birthDate, MaritalStatus maritalStatus, String country, String city, String location, String phoneNumber, String mail, String placeOfEducation, String placeOfWork, String about) {
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
	
	public static UserInfoDTO map(UserInfo userInfo) {
		return new UserInfoDTO(
				userInfo.getUser(),
				userInfo.getFirstName(),
				userInfo.getLastName(),
				userInfo.getGender(),
				userInfo.getBirthDate(),
				userInfo.getMaritalStatus(),
				userInfo.getCountry(),
				userInfo.getCity(),
				userInfo.getLocation(),
				userInfo.getPhoneNumber(),
				userInfo.getMail(),
				userInfo.getPlaceOfEducation(),
				userInfo.getPlaceOfWork(),
				userInfo.getAbout()
		);
	}
	
}
