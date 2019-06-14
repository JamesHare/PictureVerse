package com.jamesmhare.pictureverse;

import com.jamesmhare.pictureverse.listeners.TweetStreamListener;
import twitter4j.*;

/**
 * Serves as the entry point of the application.
 */
public class Application {

    public static void main(String[] args) {

        TwitterFactory twitterFactory = new TwitterFactory();
        Twitter twitter = twitterFactory.getSingleton();
        TweetStreamListener tweetStreamListener = new TweetStreamListener(twitter);

    }

}
