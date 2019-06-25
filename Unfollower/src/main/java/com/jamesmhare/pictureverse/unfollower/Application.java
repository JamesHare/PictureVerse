package com.jamesmhare.pictureverse.unfollower;

import com.jamesmhare.pictureverse.unfollower.utils.Unfollower;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import java.util.Scanner;

/**
 * Serves as the entry point of the application.
 */
public class Application {

    public static Scanner in = new Scanner(System.in);
    public static boolean menuRunning = true;

    public static void main(String[] args) {

        TwitterFactory twitterFactory = new TwitterFactory();
        Twitter twitter = twitterFactory.getSingleton();

        runMainMenu(twitter);
    }

    public static void runMainMenu(Twitter twitter) {
        do {
            printOptions();
            int choice = in.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter the Twitter User ID: ");
                    String userID = in.next();
                    Unfollower.unfollow(twitter, userID);
                    System.out.println("User " + userID + " has been unfollowed.");
                    break;
                case 2:
                    Unfollower.unfollowAll(twitter);
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Incorrect selection. Please try again.");
            }
        } while (true);
    }

    private static void printOptions() {
        System.out.println();
        System.out.println("Unfollow Options");
        System.out.println();
        System.out.println("1. Unfollow a specific user");
        System.out.println("2. Unfollow all users");
        System.out.println("3. Exit");
        System.out.println();
        System.out.print("Please choose a menu option (1-3): ");
    }

}
