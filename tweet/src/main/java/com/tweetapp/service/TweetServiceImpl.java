package com.tweetapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetapp.Exception.TweetNotFoundException;
import com.tweetapp.Exception.UserNotFoundException;
import com.tweetapp.kafka.ProducerService;
import com.tweetapp.model.Tweet;
import com.tweetapp.repository.TweetRepository;

@Service
public class TweetServiceImpl implements TweetService {

	Logger logger = LoggerFactory.getLogger(TweetServiceImpl.class);

	@Autowired
	TweetRepository tweetRepo;

	@Autowired
	ProducerService producerService;

	@Override
	public List<Tweet> getAllTweets() {

		logger.info("Retrieve all the Tweets");
		List<Tweet> tweets = tweetRepo.findAll();
		return tweets;
	}

	@Override
	public List<Tweet> getTweetsOfUser(String userName) throws Exception {
		logger.info("Get all tweets of a particular User");
		List<Tweet> tweetsOfUser = tweetRepo.findByUserUserName(userName);
		if (tweetsOfUser != null) {
			return tweetsOfUser;
		} else {
			logger.error("Throws Exception");
			throw new UserNotFoundException();
		}
	}

	@Override
	public Tweet addTweet(Tweet tweet) {
		Tweet addTweet = tweetRepo.save(tweet);
		producerService.sendMessage(LocalDateTime.now() + "New Tweet Added" + addTweet);
		logger.info("New Tweet created Successfully!!");
		return addTweet;
	}

	@Override
	public Tweet updateTweet(String id, Tweet updateTweet) throws Exception {
		logger.info("Inside Update tweet");
		Tweet tweet = tweetRepo.findById(id).orElse(null);
		if (tweet != null) {
			tweet = tweetRepo.save(updateTweet);
			producerService.sendMessage(
					LocalDateTime.now() + " - " + "updated Tweet" + " - " + tweet + " - " + tweet.getUser().getUserName());
			logger.info("Existing Tweet is updated Successfully!!");
			return tweet;
		} else {
			logger.error("Throws Exception");
			throw new TweetNotFoundException();
		}
	}

	@Override
	public String deleteTweet(String id) throws Exception {
		logger.info("Inside Delete Tweet");
		Tweet deleteTweet = tweetRepo.findById(id).orElse(null);
		if (deleteTweet != null) {
			tweetRepo.delete(deleteTweet);
			producerService.sendMessage(LocalDateTime.now() + " - " + "Deleted Tweet" + " - " +deleteTweet);
			logger.info("Deleted a Tweet");
			return "Tweet Deleted Successfully";
		} else {
			logger.error("Throws Exception");
			throw new TweetNotFoundException();
		}
	}

	@Override
	public String likeTweet(String id) throws Exception {
		logger.info("Inside Like Tweet");
		Optional<Tweet> likeTweet = tweetRepo.findById(id);
		if (likeTweet.isPresent()) {
			likeTweet.get().setLikes(likeTweet.get().getLikes() + 1);
			producerService.sendMessage(
					LocalDateTime.now() + " - " + "Liked a Tweet" + " - "+ likeTweet.get() + " - " + likeTweet.get().getLikes());
			logger.info("Liked a Tweet");
			tweetRepo.save(likeTweet.get());
			return "Liked";
		} else {
			logger.error("Throws Exception");
			throw new TweetNotFoundException();
		}
	}

	@Override
	public String replyTweet(String id, String reply) throws Exception {
		logger.info("Inside reply tweet");
		Optional<Tweet> tweetReply = tweetRepo.findById(id);
		if (tweetReply.isPresent()) {
			List<String> replies = tweetReply.get().getReplies();
			replies.add(reply);
			tweetReply.get().setReplies(replies);
			producerService.sendMessage(LocalDateTime.now() + " - " + "Liked a Tweet" + " - "+tweetReply.get() + " - "
					+ tweetReply.get().getReplies());
			logger.info("Replied to a Tweet");
			tweetRepo.save(tweetReply.get());
			return "Replied";
		} else {
			logger.error("Throws Exception");
			throw new TweetNotFoundException();
		}
	}
}
