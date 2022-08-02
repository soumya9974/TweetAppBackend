package com.tweetapp.repository;

import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends MongoRepository<Tweet, String> {

	List<Tweet> findByUserUserName(String userName);
}
