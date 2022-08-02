package com.tweetapp.service;

import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;

import java.util.List;

public interface TweetService {

	public List<Tweet> getAllTweets();

	public List<Tweet> getTweetsOfUser(String userName) throws Exception;

	public Tweet addTweet(Tweet tweet);

	public Tweet updateTweet(String id, Tweet tweetApp) throws Exception;

	public String deleteTweet(String id) throws Exception;

	public String likeTweet(String id) throws Exception;

	public String replyTweet(String id, String reply) throws Exception;

}
