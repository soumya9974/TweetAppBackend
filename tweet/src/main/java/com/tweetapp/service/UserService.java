package com.tweetapp.service;

import java.util.List;
import java.util.Map;

import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;

public interface UserService {

	public User createUser(User user) throws Exception;

	public UserResponse login(String userName, String password) throws Exception;

	public String forgotPassword(String username) throws Exception;

	public String resetPassword(String userName, String password) throws Exception;

	public List<User> getAllUsers();

	public List<User> getUserByUserName(String userName) throws Exception;
}
