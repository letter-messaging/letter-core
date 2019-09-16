package com.gmail.ivanjermakov1.messenger.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmail.ivanjermakov1.messenger.dto.enums.MaritalStatus;

import java.time.LocalDate;
import java.util.List;

public class UserInfoDto {

	public UserDto user;
	public String firstName;
	public String lastName;
	public Boolean gender;

	@JsonFormat(pattern = "yyyy-MM-dd")
	public LocalDate birthDate;

	public MaritalStatus maritalStatus;
	public String country;
	public String city;
	public String location;
	public String phoneNumber;
	public String mail;
	public String placeOfEducation;
	public String placeOfWork;
	public String about;
	public List<AvatarDto> avatars;

	public UserInfoDto() {
	}

	public UserInfoDto(UserDto user, String firstName, String lastName) {
		this.user = user;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public UserInfoDto(UserDto user, String firstName, String lastName, Boolean gender, LocalDate birthDate, MaritalStatus maritalStatus, String country, String city, String location, String phoneNumber, String mail, String placeOfEducation, String placeOfWork, String about, List<AvatarDto> avatars) {
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
		this.avatars = avatars;
	}

}
