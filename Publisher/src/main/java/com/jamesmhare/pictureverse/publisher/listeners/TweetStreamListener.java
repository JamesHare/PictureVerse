package com.jamesmhare.pictureverse.publisher.listeners;

import twitter4j.*;

import java.io.*;
import java.net.URL;

public class TweetStreamListener {

    private static final Logger LOGGER = Logger.getLogger(TweetStreamListener.class);
    private Twitter twitter;

    StatusListener statusListener = new StatusListener() {
        @Override
        public void onStatus(Status status) {
            boolean containsImage = false;
            if (status.getUser().getScreenName().equals("PictureVerse")) {
                LOGGER.info("This status was posted by @PictureVerse.");
            } else {
                if (status.getText().contains("RT ")) {
                    LOGGER.info("This is a retweet.");
                } else {
                    try {
                        MediaEntity[] media = status.getMediaEntities();
                        if (media[0] != null) {
                            URL url = new URL(media[0].getMediaURL());
                            InputStream is = url.openStream();
                            OutputStream os = new FileOutputStream("image.jpg");

                            byte[] b = new byte[2048];
                            int length;

                            while ((length = is.read(b)) != -1) {
                                os.write(b, 0, length);
                            }
                            is.close();
                            os.close();
                            containsImage = true;
                            LOGGER.info("Drink image was saved.");
                        }
                    } catch (IOException exception) {
                        LOGGER.error("The media from an incoming tweet could not be saved.");
                    }
                    try {
                        String statusText;
                        if (status.getText().contains("via /r/Earth")) {
                            String[] detachedStatus = status.getText().split("\\[");
                            statusText = detachedStatus[0];
                        } else {
                            statusText = status.getText();
                        }
                        StatusUpdate statusUpdate = new StatusUpdate(statusText);
                        if (containsImage) {
                            statusUpdate.setMedia(new File("image.jpg"));
                        }
                        twitter.updateStatus(statusUpdate);
                        LOGGER.info("Status has been successfully updated.");

                        if (containsImage) {
                            File tmpImageFile = new File("image.jpg");
                            tmpImageFile.delete();
                        }
                    } catch (TwitterException exception) {
                        LOGGER.error(exception.getMessage());
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

    public TweetStreamListener(Twitter twitter, long[] twitterIDs) {
        this.twitter = twitter;
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(statusListener);
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.follow(twitterIDs);
        twitterStream.filter(filterQuery);
    }

}
