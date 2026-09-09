package com.thermcampos.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesLoadConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Properties prop = new Properties();

    public PropertiesLoadConfig(String resourceName) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (input == null) {
                throw new IllegalStateException("Could not find " + resourceName + " on classpath");
            }
            this.prop.load(input);

            logger.info("Properties file loaded");

            if (logger.isDebugEnabled()) {
                logger.debug("PropertiesLoadConfig - Debug Mode ON!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + resourceName + ": " + e.getMessage(), e);
        }
    }

    public String get(String key, String fallback) {
        if (logger.isDebugEnabled()) {
            logger.debug("get - key={} fallback={}", key, fallback);
        }
        String envOverride = System.getenv(toEnvKey(key));
        if (envOverride != null && !envOverride.isBlank()) {
            if (logger.isDebugEnabled()) {
                logger.debug("get - found override={}", envOverride);
            }
            return envOverride;
        }

        return prop.getProperty(key, fallback);
    }

    public int getInt(String key, int fallback) {
        return Integer.parseInt(get(key, String.valueOf(fallback)));
    }

    public boolean getBoolean(String key, boolean fallback) {
        return Boolean.parseBoolean(get(key, String.valueOf(fallback)));
    }

    private String toEnvKey(String key) {
        if (logger.isDebugEnabled()) {
            logger.debug("toEnvKey - key={}", key);
        }
        return key.toUpperCase().replace('.','_');
    }
}

