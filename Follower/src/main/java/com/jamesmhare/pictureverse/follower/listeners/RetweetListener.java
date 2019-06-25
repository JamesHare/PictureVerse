package com.jamesmhare.pictureverse.follower.listeners;


import com.jamesmhare.pictureverse.follower.properties.ApplicationProperties;
import org.apache.log4j.Logger;
import twitter4j.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RetweetListener {

    private static final Logger LOGGER = Logger.getLogger(RetweetListener.class);
    private Twitter twitter;
    private final static int DAILY_LIMIT = Integer.parseInt(ApplicationProperties.getProperty("DailyLimit"));
    private AtomicInteger currentDailyFollowed;
    private AtomicBoolean sleeping;

    StatusListener statusListener = new StatusListener() {
        @Override
        public void onStatus(Status status) {
            if (status.getText().contains("RT")) {
                if (currentDailyFollowed.intValue() < DAILY_LIMIT) {
                    try {
                        twitter.createFriendship(status.getUser().getId());
                        LOGGER.info("Friendship with " + status.getUser().getId() + " was created.");
                        currentDailyFollowed.incrementAndGet();
                    } catch (TwitterException exception) {
                        LOGGER.error("Failed to follow user " + status.getUser().getId() + ". " + exception.getMessage());
                    }
                } else if (currentDailyFollowed.intValue() >= DAILY_LIMIT && !sleeping.get()) {
                    try {
                        sleeping.compareAndSet(false, true);
                        LOGGER.info("Daily follow limit reached. Sleeping for 1 day.");
                        TimeUnit.DAYS.sleep(1);
                        currentDailyFollowed.set(0);
                        TimeUnit.SECONDS.sleep(5);
                        sleeping.compareAndSet(true, false);
                    } catch (InterruptedException exception) {
                        sleeping.set(false);
                        LOGGER.error("Sleeping thread has been interrupted." + exception.getMessage());
                    }
                }
            }
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            // LOGGER.info("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
        }

        @Override
        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            // LOGGER.info("Got track limitation notice:" + numberOfLimitedStatuses);
        }

        @Override
        public void onScrubGeo(long userId, long upToStatusId) {
            // LOGGER.info("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
        }

        @Override
        public void onStallWarning(StallWarning warning) {
            // LOGGER.warn("Got stall warning:" + warning);
        }

        @Override
        public void onException(Exception exception) {
            // LOGGER.error(exception.getMessage());
        }
    };

    public RetweetListener(Twitter twitter, long[] twitterIDs) {
        this.twitter = twitter;
        currentDailyFollowed = new AtomicInteger(0);
        sleeping = new AtomicBoolean(false);
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(statusListener);
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.follow(twitterIDs);
        twitterStream.filter(filterQuery);
    }
}