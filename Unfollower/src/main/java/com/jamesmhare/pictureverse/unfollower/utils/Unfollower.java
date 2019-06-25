package com.jamesmhare.pictureverse.unfollower.utils;

import twitter4j.IDs;
import twitter4j.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.concurrent.TimeUnit;

public class Unfollower {

    private static final Logger LOGGER = Logger.getLogger(Unfollower.class);

    public static void unfollow(Twitter twitter, String twitterID) {
        try {
            twitter.destroyFriendship(Long.parseLong(twitterID));
        } catch (TwitterException exception) {
            LOGGER.error("Failed to unfollow user " + twitterID + " " + exception.getMessage());
        }
    }

    public static void unfollowAll(Twitter twitter) {
        boolean isFollowing = true;
        do {
            long cursor = -1L;
            IDs ids = null;
            do {
                try {
                    ids = twitter.getFollowersIDs(cursor);
                    if (ids.getIDs().length == 0 ) {
                        isFollowing = false;
                    }
                } catch (TwitterException exception) {
                    isFollowing = false;
                    LOGGER.error("Failed to retrieve the list of friends." + exception.getMessage());
                }
                for (long userID : ids.getIDs()) {
                    try {
                        twitter.destroyFriendship(userID);
                    } catch (TwitterException exception) {
                        LOGGER.error("Failed to unfollow user " + userID + ". " + exception.getMessage());
                    }
                }
            } while ((cursor = ids.getNextCursor()) != 0);
            try {
                LOGGER.info("Unfollower is sleeping for 2 hours.");
                TimeUnit.HOURS.sleep(2);
                LOGGER.info("Unfollowre is resuming");
            } catch (InterruptedException exception) {
                LOGGER.error("Sleep during unfollow was interrupted. " + exception.getMessage());
            }
        } while (isFollowing);
    }

}
