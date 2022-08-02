package com.tweetapp.model;

public class UserResponse {

	private User user;
	private String status;
	private String token;

	public UserResponse(User user, String status, String token) {
		super();
		this.user = user;
		this.status = status;
		this.token = token;
	}

	public UserResponse() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
