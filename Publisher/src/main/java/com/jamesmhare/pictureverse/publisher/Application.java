package com.jamesmhare.pictureverse.publisher;

import com.jamesmhare.pictureverse.publisher.constants.ApplicationConstants;
import com.jamesmhare.pictureverse.publisher.listeners.TweetStreamListener;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

/**
 * Serves as the entry point of the application.
 */
public class Application {

    public static void main(String[] args) {

        TwitterFactory twitterFactory = new TwitterFactory();
        Twitter twitter = twitterFactory.getSingleton();

        TweetStreamListener tweetStreamListener = new TweetStreamListener(twitter, ApplicationConstants.getAccounts());

    }
}
