package com.blogapp.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerConfig {
    private LoggerConfig() {
        // Prevent instantiation
    }

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
