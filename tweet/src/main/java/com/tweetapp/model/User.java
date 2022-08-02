package com.tweetapp.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Document(collection = "User")
@ApiModel(value = "Model class to store Users")
public class User {

	@Id
	@NotNull
	@UniqueElements
	@ApiModelProperty(notes = "unique username of a user")
	private String userName;
	@NotNull
	@ApiModelProperty(notes = "firstName of a user")
	private String firstName;
	@NotNull
	@ApiModelProperty(notes = "username of a user")
	private String lastName;
	@NotNull
	@UniqueElements
	@ApiModelProperty(notes = "unique email of a user")
	private String email;
	@NotNull
	@ApiModelProperty(notes = "password of a user")
	private String password;
	@NotNull
	@ApiModelProperty(notes = "contact number of a user")
	private String contactNumber;

	public User(String userName, String firstName, String lastName, String email, String password,
			String contactNumber) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.contactNumber = contactNumber;
	}

	public User() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	@Override
	public String toString() {
		return "User{" + ", userName='" + userName + '\'' + ", firstName='" + firstName + '\'' + ", lastName='"
				+ lastName + '\'' + ", email='" + email + '\'' + ", password='" + password + '\'' + ", contactNumber="
				+ contactNumber + '}';
	}
}
