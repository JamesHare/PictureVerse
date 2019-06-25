package com.jamesmhare.pictureverse.publisher.properties;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class serves as a helper class to retrieve properties set in the
 * config.properties file.
 *
 * @author James Hare
 */
public class ApplicationProperties {
    private static final Logger LOGGER = Logger.getLogger(ApplicationProperties.class);

    public ApplicationProperties() {}

    /**
     * Returns the value associated with the given key from the properties file.
     *
     * @param key = A {@link String} specifying the key.
     * @return the value associated with the key.
     */
    public static String getProperty(String key) {
        Properties properties = new Properties();
        InputStream input = null;
        StringBuilder value = new StringBuilder("");
        try {
            input = new FileInputStream("src/main/resources/config.properties");
            properties.load(input);
            value.append(properties.getProperty(key));
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException exception) {
                    LOGGER.error(exception.getMessage());
                }
            }
        }
        return value.toString();
    }

}