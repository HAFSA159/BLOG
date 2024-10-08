package com.blogapp.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

import com.blogapp.config.LoggerConfig;

public class ValidationUtil {
    private static final Logger logger = LoggerConfig.getLogger(ValidationUtil.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_AGE = 18;

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            logger.warn("Email validation failed: email is null or empty");
            return false;
        }
        boolean isValid = EMAIL_PATTERN.matcher(email).matches();
        if (!isValid) {
            logger.warn("Email validation failed for: {}", email);
        }
        return isValid;
    }

    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            logger.warn("Name validation failed: name is null or empty");
            return false;
        }
        boolean isValid = name.length() >= MIN_NAME_LENGTH && name.length() <= MAX_NAME_LENGTH;
        if (!isValid) {
            logger.warn("Name validation failed for: {}", name);
        }
        return isValid;
    }

    public static boolean isValidBirthdate(LocalDate birthdate) {
        if (birthdate == null) {
            logger.warn("Birthdate validation failed: birthdate is null");
            return false;
        }
        LocalDate minValidDate = LocalDate.now().minusYears(MIN_AGE);
        boolean isValid = birthdate.isBefore(minValidDate) || birthdate.isEqual(minValidDate);
        if (!isValid) {
            logger.warn("Birthdate validation failed for: {}", birthdate);
        }
        return isValid;
    }

    public static boolean isValidArticleTitle(String title) {
        if (title == null || title.isEmpty()) {
            logger.warn("Article title validation failed: title is null or empty");
            return false;
        }
        boolean isValid = title.length() >= 5 && title.length() <= 200;
        if (!isValid) {
            logger.warn("Article title validation failed for: {}", title);
        }
        return isValid;
    }

    public static boolean isValidArticleContent(String content) {
        if (content == null || content.isEmpty()) {
            logger.warn("Article content validation failed: content is null or empty");
            return false;
        }
        boolean isValid = content.length() >= 50;
        if (!isValid) {
            logger.warn("Article content validation failed: content is too short");
        }
        return isValid;
    }
}