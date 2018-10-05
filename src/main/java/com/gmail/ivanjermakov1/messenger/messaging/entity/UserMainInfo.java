package com.gmail.ivanjermakov1.messenger.messaging.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_main_info")
public class UserMainInfo {
	
	@Id
	@JoinColumn(name = "id",
			foreignKey = @ForeignKey(name = "user_main_into_user_id_fk")
	)
	private Long id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	public UserMainInfo() {
	}
	
	public UserMainInfo(Long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
}
