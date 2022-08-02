package com.tweetapp.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Document(collection = "Tweet")
@ApiModel(value = "Model class to store tweets")
public class Tweet {

	@Id
	@UniqueElements
	@NotNull
	@ApiModelProperty(notes = "Id of a tweet")
	private String id;
	@ApiModelProperty(notes = "Tweet Description of tweet")
	@Size(min = 3, max = 144, message = "Tweet Should contain more than 144 characters")
	private String tweet;
	@ApiModelProperty(notes = "User of a tweet")
	private User user;
	@ApiModelProperty(notes = "Likes of a tweet")
	private long likes;
	@ApiModelProperty(notes = "Date of a tweet")
	private LocalDateTime postDate;
	@ApiModelProperty(notes = "Replies to a tweet")
	private List<String> replies;
	@Size(min = 3, max = 50, message = "tweetTag should not contain more than 50 characters")
	private String tweetTag;

	public Tweet(String id, String tweet, User user, long likes, LocalDateTime postDate, List<String> replies,
			String tweetTag) {
		this.id = id;
		this.tweet = tweet;
		this.user = user;
		this.likes = likes;
		this.postDate = postDate;
		this.replies = replies;
		this.tweetTag = tweetTag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getLikes() {
		return likes;
	}

	public void setLikes(long likes) {
		this.likes = likes;
	}

	public LocalDateTime getPostDate() {
		return postDate;
	}

	public void setPostDate(LocalDateTime postDate) {
		this.postDate = postDate;
	}

	public List<String> getReplies() {
		return replies;
	}

	public void setReplies(List<String> replies) {
		this.replies = replies;
	}

	public String getTweetTag() {
		return tweetTag;
	}

	public void setTweetTag(String tweetTag) {
		this.tweetTag = tweetTag;
	}

	@Override
	public String toString() {
		return "Tweet{" + "id='" + id + '\'' + ", tweet='" + tweet + '\'' + ", user=" + user + ", likes=" + likes
				+ ", postDate=" + postDate + ", replies=" + replies + ", tweetTag='" + tweetTag + '\'' + '}';
	}
}
