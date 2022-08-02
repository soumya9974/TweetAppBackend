package com.tweetapp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.Exception.InvalidUserNameorPasswordException;
import com.tweetapp.Exception.TweetNotFoundException;
import com.tweetapp.Exception.UserAlreadyExistsException;
import com.tweetapp.Exception.UserNotFoundException;
import com.tweetapp.controller.TweetAppController;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.service.JWTUtil;
import com.tweetapp.service.TweetService;
import com.tweetapp.service.UserService;

@WebMvcTest(value = TweetAppController.class)
public class ControllerTests {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	TweetService tweetService;

	@MockBean
	UserService userService;

	@MockBean
	JWTUtil util;

	private User user1;
	private User user2;
	private Tweet tweet1;
	private Tweet tweet2;
	private Tweet tweet3;
	private UserResponse response1;

	@BeforeEach
	public void setup() {
		user1 = new User("Soumya@1", "Soumya", "Kappera", "soumya123@gmail.com", "Soumya@99", "9876543210");
		user2 = new User("Karishma@1", "Karishma", "Mahammad", "karishma23@gmail.com", "Karishma@1", "9876543222");
		tweet1 = new Tweet("12", "App", user1, 123L, LocalDateTime.now(), new ArrayList<>(), "Monsoon");
		tweet2 = new Tweet("14", "Name", user2, 145L, LocalDateTime.now(), new ArrayList<>(), "New");
		response1 = new UserResponse(user1, "Login Success", "sbafkbHBAFJ1");
	}

	/**************************
	 * registerUser()
	 ************************************************************/
	@Test
	@DisplayName("Test valid registerUser()")
	public void testValidRegisterUser() throws Exception {
		when(userService.createUser(any())).thenReturn(user1);
		this.mockMvc.perform(post("/api/v1.0/tweets/register").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(user1))).andExpect(status().isCreated());
		verify(userService, times(1)).createUser(any());
	}

	@Test
	@DisplayName("Test Invalid registerUser()")
	public void testInvalidRegisterUser() throws Exception {
		when(userService.createUser(any())).thenThrow(new UserAlreadyExistsException());
		this.mockMvc.perform(post("/api/v1.0/tweets//register").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(user2))).andExpect(status().isBadRequest());
		verify(userService, times(1)).createUser(any());
	}

	/*************************************
	 * login()
	 ***************************************/
	@Test
	@DisplayName("Test valid Login()")
	public void testValidLogin() throws Exception {
		when(userService.login(any(), any())).thenReturn(response1);
		this.mockMvc.perform(get("/api/v1.0/tweets/login").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(user1))).andExpect(status().isOk());
		verify(userService, times(1)).login(any(), any());
	}

	@Test
	@DisplayName("Test Invalid Login()")
	public void testInvalidLogin() throws Exception {
		when(userService.login(any(), any())).thenThrow(new InvalidUserNameorPasswordException());
		this.mockMvc.perform(post("/api/v1.0/tweets/login").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(null))).andExpect(status().isBadRequest());
		verify(userService, times(1)).login(any(), any());
	}

	/****************************
	 * forgotPassword()
	 *****************************************/
	@Test
	@DisplayName("Test forgotPassword()")
	public void testForgotPassword() throws Exception {
		when(userService.resetPassword(any(), any())).thenReturn("Reset Password Successful");
		this.mockMvc.perform(put("/api/v1.0/tweets/Soumya@1/forgot")).andExpect(status().isOk());
		verify(userService, times(1)).resetPassword(any(), any());
	}

	/*******************************************
	 * validateToken()
	 *****************************************/
	@Test
	@DisplayName("Test validateToken()")
	public void testValidateToken() throws Exception {
		when(util.isValidToken(any())).thenReturn(true);
		this.mockMvc.perform(get("/api/v1.0/tweets/validate")).andExpect(status().isOk());
	}

	/************************************************
	 * gatAllUsers()
	 *******************************************/
	@Test
	@DisplayName("Test getAllUsers()")
	public void testGetAllUsers() throws Exception {
		when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
		this.mockMvc.perform(get("/api/v1.0/tweets/users/all")).andExpect(status().isOk());
		verify(userService, times(1)).getAllUsers();
	}

	/*************************************
	 * searchByUserName()
	 ***********************************************/
	@Test
	@DisplayName("Test  valid searchByUserName()")
	public void testValidSearchByUserName() throws Exception {
		when(userService.getUserByUserName(any())).thenReturn(Arrays.asList(user1, user2));
		this.mockMvc.perform(get("/api/v1.0/tweets/user/search/@1")).andExpect(status().isFound());
		verify(userService, times(1)).getUserByUserName("@1");
	}

	@Test
	@DisplayName("Test Invalid searchByUserName()")
	public void testInvalidSearchByUserName() throws Exception {
		when(userService.getUserByUserName(any())).thenThrow(new InvalidUserNameorPasswordException());
		this.mockMvc.perform(get("/api/v1.0/tweets/user/search/@1Shyam")).andExpect(status().isBadRequest());
		verify(userService, times(1)).getUserByUserName(any());
	}

	/*****************************************
	 * getAllTweets()
	 **********************************************/
	@Test
	@DisplayName("Test getAllTweets()")
	public void testGetAllTweets() throws Exception {
		when(tweetService.getAllTweets()).thenReturn(Arrays.asList(tweet1));
		this.mockMvc.perform(get("/api/v1.0/tweets/all")).andExpect(status().isOk());
		verify(tweetService, times(1)).getAllTweets();
	}

	/***********************************
	 * getAllTweetsofUser()
	 ****************************************/

	@Test
	@DisplayName("Test valid getAllTweetsOfUser()")
	public void testValidGetAllTweetsOfUser() throws Exception {
		when(tweetService.getTweetsOfUser("Soumya@1")).thenReturn(List.of(tweet1));
		this.mockMvc.perform(get("/api/v1.0/tweets/{userName}", "Soumya@1")).andExpect(status().isFound());
		verify(tweetService, times(1)).getTweetsOfUser(any());
	}

	@Test
	@DisplayName("Test Invalid getAllTweetsOfUser()")
	public void testInvalidGetAllTweetsOfUser() throws Exception {
		when(tweetService.getTweetsOfUser(any())).thenThrow(new UserNotFoundException());
		this.mockMvc.perform(get("/api/v1.0/tweets/Shyam")).andExpect(status().isBadRequest());
		verify(tweetService, times(1)).getTweetsOfUser(any());
	}

	/*************************************
	 * postTweet() //
	 *******************************************/
//	@Test
//	@DisplayName("Test valid postTweet()")
//	public void testValidPostTweet() throws Exception {
//		tweet3 = new Tweet("36", "Lotus", user2, 1400L, LocalDateTime.now(), Arrays.asList("Hi"), "@Flower");
//		when(tweetService.addTweet(tweet3)).thenReturn(tweet3);
//		this.mockMvc.perform(post("/api/v1.0/tweets/{username}/add", "Karishma@1").contentType(MediaType.APPLICATION_JSON)
//				.content(new ObjectMapper().writeValueAsString(tweet3))).andExpect(status().isCreated());
//		verify(tweetService, times(1)).addTweet(any());
//	}

	/*********************************
	 * updateTweet()
	 ***********************************************/
//	@Test
//	@DisplayName("Test valid updateTweet()")
//	public void testValidUpdateTweet() throws Exception {
//		tweet3 = new Tweet("12", "Lotus", user1, 1400L, LocalDateTime.now(), Arrays.asList("Flora"), "@Flower");
//		when(tweetService.updateTweet("12",tweet3)).thenReturn(tweet3);
//		this.mockMvc.perform(put("/api/v1.0/tweets/{username}/update/{id}","Soumya@1","12").contentType(MediaType.APPLICATION_JSON)
//				.content(new ObjectMapper().writeValueAsString(tweet3))).andExpect(status().isOk());
//		verify(tweetService, times(1)).updateTweet(any(), any());
//	}

	/************************************************
	 * deleteTweet()
	 *********************************************/
	@Test
	@DisplayName("Test valid deleteTweet()")
	public void testValidDeleteTweet() throws Exception {
		when(tweetService.deleteTweet(any())).thenReturn("Tweet Deleted Successfully");
		this.mockMvc.perform(delete("/api/v1.0/tweets/Soumya@1/delete/12")).andExpect(status().isOk());
		verify(tweetService, times(1)).deleteTweet(any());
	}

	@Test
	@DisplayName("Test Invalid deleteTweet()")
	public void testInvalidDeleteTweet() throws Exception {
		when(tweetService.deleteTweet(any())).thenThrow(new TweetNotFoundException());
		this.mockMvc.perform(delete("/api/v1.0/tweets/Soumya@1/delete/15")).andExpect(status().isBadRequest());
		verify(tweetService, times(1)).deleteTweet(any());
	}

	/******************************************************
	 * likeTweet()
	 ******************************************/
	@Test
	@DisplayName("Test valid likeTweet()")
	public void testValidLikeTweet() throws Exception {
		when(tweetService.likeTweet(any())).thenReturn("Liked");
		this.mockMvc.perform(put("/api/v1.0/tweets/Soumya@1/like/12")).andExpect(status().isOk());
		verify(tweetService, times(1)).likeTweet(any());
	}

	@Test
	@DisplayName("Test Invalid likeTweet()")
	public void testInvalidLikeTweet() throws Exception {
		when(tweetService.likeTweet(any())).thenThrow(new TweetNotFoundException());
		this.mockMvc.perform(put("/api/v1.0/tweets/Shyam/like/15")).andExpect(status().isBadRequest());
		verify(tweetService, times(1)).likeTweet(any());
	}

	/*******************************************************
	 * replyTweet()
	 ****************************************/
	@Test
	@DisplayName("Test valid replyTweet()")
	public void testValidReplyTweet() throws Exception {
		when(tweetService.replyTweet(any(), any())).thenReturn("Replied");
		this.mockMvc.perform(post("/api/v1.0/tweets/Karishma@1/reply/14").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tweet2))).andExpect(status().isOk());
		verify(tweetService, times(1)).replyTweet(any(), any());
	}

	@Test
	@DisplayName("Test Invalid replyTweet()")
	public void testInvalidReplyTweet() throws Exception {
		when(tweetService.replyTweet(any(), any())).thenThrow(new TweetNotFoundException());
		this.mockMvc.perform(post("/api/v1.0/tweets/Karishma/reply/15").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tweet2))).andExpect(status().isBadRequest());
		verify(tweetService, times(1)).replyTweet(any(), any());
	}

}
