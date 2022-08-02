package com.tweetapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.tweetapp.Exception.InvalidUserNameorPasswordException;
import com.tweetapp.Exception.TweetNotFoundException;
import com.tweetapp.Exception.UserAlreadyExistsException;
import com.tweetapp.Exception.UserNotFoundException;
import com.tweetapp.kafka.ProducerService;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.model.UserResponse;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.JWTUtil;
import com.tweetapp.service.TweetServiceImpl;
import com.tweetapp.service.UserServiceImpl;

public class ServiceTests {

	@Autowired
	MockMvc mockMvc;

	@InjectMocks
	TweetServiceImpl tweetService;

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	JWTUtil util;

	@Mock
	TweetRepository tweetRepo;

	@Mock
	ProducerService producerService;

	@Mock
	UserRepository userRepo;

	@SuppressWarnings("deprecation")
	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	private User user1;
	private User user2;
	private User user3;
	private Tweet tweet1;
	private Tweet tweet2;
	private Tweet tweet3;
	private Tweet tweet4;
	private UserResponse response1;
	private UserResponse response2;

	@BeforeEach
	public void setup() {
		user1 = new User("Soumya@1", "Soumya", "Kappera", "soumya123@gmail.com", "Soumya@99", "9876543210");
		user2 = new User("Karishma@1", "Karishma", "Mahammad", "karishma23@gmail.com", "Karishma@1", "9876543222");
		tweet1 = new Tweet("12", "App", user1, 123L, LocalDateTime.now(), new ArrayList<>(), "Monsoon");
		tweet2 = new Tweet("14", "Name", user2, 145L, LocalDateTime.now(), new ArrayList<>(), "New");
		tweet4 = new Tweet("14", "Name", user2, 1455L, LocalDateTime.now(), new ArrayList<>(), "Old");
		response1 = new UserResponse(user1, "Login Success", "sbafkbHBAFJ1");
		response2 = new UserResponse(null, "Login Failed", null);
	}

	@Test
	@DisplayName("Test getAllTweets() of TweetService")
	public void testGetAllTweets() {
		when(tweetRepo.findAll()).thenReturn(Arrays.asList(tweet1, tweet2));
		assertThat(tweetService.getAllTweets()).hasSize(2);
	}

	@Test
	@DisplayName("Test Valid getTweetsOfUser() of TweetService")
	public void testValidGetTweetsOfUser() throws Exception {
		when(tweetRepo.findByUserUserName("Soumya@1")).thenReturn(Arrays.asList(tweet1));
		assertTrue(tweetService.getTweetsOfUser("Soumya@1").equals(Arrays.asList(tweet1)));
		verify(tweetRepo).findByUserUserName("Soumya@1");
	}

	@Test
	@DisplayName("Test Invalid getTweetsOfUser() of TweetService")
	public void testInvalidGetTweetsOfUser() throws Exception {
		when(tweetRepo.findByUserUserName("Sam")).thenReturn(null);
		assertThrows(UserNotFoundException.class, () -> {
			tweetService.getTweetsOfUser("Sam");
		});
		verify(tweetRepo).findByUserUserName("Sam");
	}

	@Test
	@DisplayName("Test addTweet() of TweetService")
	public void testAddTweet() {
		tweet4 = new Tweet("36", "Lotus", user2, 1400L, LocalDateTime.now(), Arrays.asList("Hi", "Hello"), "@Flower");
		when(tweetRepo.save(tweet4)).thenReturn(tweet4);
		assertTrue(tweetService.addTweet(tweet4).equals(tweet4));
		verify(tweetRepo).save(tweet4);
	}

	@Test
	@DisplayName("Test Valid updateTweet() of TweetService")
	public void testValidUpdateTweet() throws Exception {
		when(tweetRepo.findById(tweet2.getId())).thenReturn(Optional.of(tweet4));
		when(tweetRepo.save(tweet4)).thenReturn(tweet4);
		assertTrue(tweetService.updateTweet(tweet2.getId(), tweet4).equals(tweet4));
		verify(tweetRepo).findById(tweet2.getId());
	}

	@Test
	@DisplayName("Test InValid updateTweet() of TweetService ")
	public void testInValidUpdateTweet() throws Exception {
		when(tweetRepo.findById("20")).thenReturn(Optional.empty());
		assertThrows(TweetNotFoundException.class, () -> {
			tweetService.updateTweet("20", tweet3);
		});
		verify(tweetRepo).findById("20");
	}

	@Test
	@DisplayName("Test Valid deleteTweet() of TweetService")
	public void testValidDeleteTweet() throws Exception {
		when(tweetRepo.findById("12")).thenReturn(Optional.of(tweet1));
		assertTrue(tweetService.deleteTweet("12").equals("Tweet Deleted Successfully"));
		verify(tweetRepo).findById("12");
	}

	@Test
	@DisplayName("Test Invalid deleteTweet() of TweetService")
	public void testInValidDeleteTweet() throws Exception {
		when(tweetRepo.findById("38")).thenReturn(Optional.empty());
		assertThrows(TweetNotFoundException.class, () -> {
			tweetService.deleteTweet("38");
		});
		verify(tweetRepo).findById("38");
	}

	@Test
	@DisplayName("Test Valid likeTweet() of TweetService")
	public void testValidLikeTweet() throws Exception {
		when(tweetRepo.findById("14")).thenReturn(Optional.of(tweet2));
		assertTrue(tweetService.likeTweet("14").equals("Liked"));
		verify(tweetRepo).findById("14");

	}

	@Test
	@DisplayName("Test Invalid likeTweet() of TweetService")
	public void testInValidLikeTweet() throws Exception {
		when(tweetRepo.findById("38")).thenReturn(Optional.empty());
		assertThrows(TweetNotFoundException.class, () -> {
			tweetService.likeTweet("38");
		});
		verify(tweetRepo).findById("38");
	}

	@Test
	@DisplayName("Test valid replytweet() of TweetService")
	public void testValidReplyTweet() throws Exception {
		when(tweetRepo.findById("14")).thenReturn(Optional.of(tweet2));
		assertTrue(tweetService.replyTweet("14", "Beauty").equals("Replied"));
		verify(tweetRepo).findById("14");
	}

	@Test
	@DisplayName("Test Invalid reply tweet of TweetService")
	public void testInvalidReplyTweet() throws Exception {
		when(tweetRepo.findById("38")).thenReturn(Optional.empty());
		assertThrows(TweetNotFoundException.class, () -> {
			tweetService.replyTweet("38", "Beauty");
		});
		verify(tweetRepo).findById("38");
	}

	/**************************** USER SERVICE TESTS *****************************/

	@Test
	@DisplayName("Test valid createUser() of UserService")
	public void testValidCreateUser() throws Exception {
		when(userRepo.save(user2)).thenReturn(user2);
		assertTrue(userService.createUser(user2).equals(user2));
		verify(userRepo).save(user2);
	}

	@Test
	@DisplayName("Test Invalid createUser() of UserService")
	public void testInvalidCreateUser() throws Exception {
		User user4 = new User("Karishma@1", "Karishma", "Mahammad", "karishma23@gmail.com", "Karishma@1", "9876543222");
		when(userRepo.findByUserName("Karishma@1")).thenReturn(user2);
		assertThrows(UserAlreadyExistsException.class, () -> {
			userService.createUser(user4);
		});
		verify(userRepo).findByUserName("Karishma@1");
	}

	@Test
	@DisplayName("Test getAllUsers() of UserService")
	public void testGetAllUsers() {
		when(userRepo.findAll()).thenReturn(Arrays.asList(user1, user2));
		assertThat(userService.getAllUsers()).hasSize(2);
	}

	@Test
	@DisplayName("Test valid getUserByUserName() of UserService")
	public void testValidGetUserByUserName() throws Exception {
		when(userRepo.findByUserNameContaining("@1")).thenReturn(Arrays.asList(user1, user2));
		assertTrue(userService.getUserByUserName("@1").equals(Arrays.asList(user1, user2)));
		verify(userRepo, times(2)).findByUserNameContaining("@1");
	}

	@Test
	@DisplayName("Test Invalid getUserByUserName() of UserService")
	public void testInvalidGetUserByUserName() throws Exception {
		when(userRepo.findByUserNameContaining("Singh")).thenReturn(null);
		assertThrows(InvalidUserNameorPasswordException.class, () -> {
			userService.getUserByUserName("Singh");
		});
		verify(userRepo).findByUserNameContaining("Singh");
	}

	/*********************** login() Tests ***************************/
	@Test
	@DisplayName("Test valid login() of UserService")
	public void testValidLogin() throws Exception {
		when(userRepo.findByUserName("Soumya@1")).thenReturn(user1);
		when(util.createToken(user1.getUserName())).thenReturn("sbafkbHBAFJ1");
		assertThat(userService.login("Soumya@1", "Soumya@99").getUser().getUserName())
				.isEqualTo(response1.getUser().getUserName());
		assertThat(userService.login("Soumya@1", "Soumya@99").getToken()).isEqualTo(response1.getToken());
		verify(userRepo, times(2)).findByUserName("Soumya@1");
		verify(util, times(2)).createToken(user1.getUserName());
	}

	@Test
	@DisplayName("Test Invalid userName login() of UserService")
	public void testInvalidUsernameLogin() throws Exception {
		when(userRepo.findByUserName("Shyam")).thenReturn(null);
		assertThrows(InvalidUserNameorPasswordException.class, () -> {
			userService.login("Shyam", "Shyam");
		});
		verify(userRepo).findByUserName("Shyam");
	}

	@Test
	@DisplayName("Test Invalid password login() of UserService")
	public void testInvalidPasswordLogin() throws Exception {
		when(userRepo.findByUserName("Soumya@1")).thenReturn(user1);
		when(util.createToken("user1")).thenReturn(null);
		assertThrows(InvalidUserNameorPasswordException.class, () -> {
			userService.login("Soumya@1", "Shyam");
		});
		verify(userRepo).findByUserName("Soumya@1");
	}

	/************************** forgotPassword() ********************************/
	@Test
	@DisplayName("Test valid forgotPassword() of UserService")
	public void testValidForgotPassword() throws Exception {
		when(userRepo.findByUserName("Soumya@1")).thenReturn(user1);
		assertTrue(userService.forgotPassword("Soumya@1").equals("Random UUID Password created"));
		verify(userRepo).findByUserName("Soumya@1");
	}

	@Test
	@DisplayName("Test Invalid forgotPassword() of UserService")
	public void testInvalidForgotPassword() throws Exception {
		when(userRepo.findByUserName("Soumya")).thenReturn(null);
		assertThrows(UserNotFoundException.class, () -> {
			userService.forgotPassword("Soumya");
		});
		verify(userRepo).findByUserName("Soumya");
	}

	/***************************** resetPassword() ******************************/
	@Test
	@DisplayName("Test valid resetPassword() of UserService")
	public void testValidResetPassword() throws Exception {
		when(userRepo.findByUserName("Soumya@1")).thenReturn(user1);
		assertTrue(userService.resetPassword("Soumya@1", "NewPass").equals("Reset Password Successfully"));
		verify(userRepo).findByUserName("Soumya@1");
	}

	@Test
	@DisplayName("Test Invalid restPassword() of UserService")
	public void testInvalidResetPassword() throws Exception {
		when(userRepo.findByUserName("Soumya")).thenReturn(null);
		assertThrows(UserNotFoundException.class, () -> {
			userService.resetPassword("Soumya", "Soumya@99");
		});
		verify(userRepo).findByUserName("Soumya");
	}

}
