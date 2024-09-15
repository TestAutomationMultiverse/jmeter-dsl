package com.example.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class utilities {

        private static final Logger LOGGER = LogManager.getLogger(utilities.class);

        public static String loadProperty(String string) {
                try (InputStream resource = utilities.class.getClassLoader().getResourceAsStream("global.properties")) {
                        Properties properties = new Properties();
                        properties.load(resource);
                        String value = properties.getProperty(string);
                        LOGGER.debug("Loaded property {} with value {}", string, value);
                        return value;
                } catch (IOException e) {
                        LOGGER.error("Could not read " + string + " from resources", e);
                        throw new UncheckedIOException("Could not read " + string + " from resources", e);
                }
        }

}

