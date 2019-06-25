package com.jamesmhare.pictureverse.follower;

import com.jamesmhare.pictureverse.follower.constants.ApplicationConstants;
import com.jamesmhare.pictureverse.follower.listeners.RetweetListener;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

/**
 * Serves as the entry point of the application.
 */
public class Application {

    public static void main(String[] args) {

        TwitterFactory twitterFactory = new TwitterFactory();
        Twitter twitter = twitterFactory.getSingleton();

        RetweetListener retweetListener = new RetweetListener(twitter, ApplicationConstants.getAccounts());

    }
}
