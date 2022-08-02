package com.tweetapp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.Exception.InvalidUserNameorPasswordException;
import com.tweetapp.Exception.UserAlreadyExistsException;
import com.tweetapp.Exception.UserNotFoundException;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepo;

	@Autowired
	JWTUtil util;

	@Override
	public User createUser(User user) throws Exception {
		logger.info("Registration of a new User");
		if (userRepo.findByUserName(user.getUserName()) != null) {
			throw new UserAlreadyExistsException();
		}
		User newUser = userRepo.save(user);
		logger.info("Registered Successfully!!");
		return newUser;
	}

	@Override
	public UserResponse login(String userName, String password) throws Exception {
		logger.debug("Logging In");
		UserResponse response = new UserResponse();
		User user = userRepo.findByUserName(userName);
		if (user != null) {
			if (user.getPassword().equals(password)) {
				response.setUser(user);
				response.setStatus("Login Success");
				response.setToken(util.createToken(user.getUserName()));
				return response;
			} else
				throw new InvalidUserNameorPasswordException();
		} else
			throw new InvalidUserNameorPasswordException();
	}

	@Override
	public String forgotPassword(String userName) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		User user = userRepo.findByUserName(userName);
		if (user != null) {
			user.setPassword(UUID.randomUUID().toString());
			userRepo.save(user);
			map.put("newPassword", user.getPassword());
			map.put("resetStatus", "Success");
			logger.info("Password reset Success for forgot Password");
			return "Random UUID Password created";
		} else
			throw new UserNotFoundException();
	}

	@Override
	public String resetPassword(String userName, String password) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		User user = userRepo.findByUserName(userName);
		if (user != null) {
			user.setPassword(password);
			userRepo.save(user);
			map.put("newPassword", user.getPassword());
			map.put("resetStatus", "Success");
			logger.info("Password  Reset Successfully");
			return "Reset Password Successfully";
		} else
			throw new UserNotFoundException();
	}

	@Override
	public List<User> getAllUsers() {
		logger.info("Retrieve all the users");
		List<User> users = userRepo.findAll();
		return users;
	}

	@Override
	public List<User> getUserByUserName(String userName) throws Exception {
		logger.info("Retrieve User from the UserName");
		if (userRepo.findByUserNameContaining(userName) == null) {
			throw new InvalidUserNameorPasswordException();
		} else {
			List<User> user = userRepo.findByUserNameContaining(userName);
			return user;
		}
	}

}
