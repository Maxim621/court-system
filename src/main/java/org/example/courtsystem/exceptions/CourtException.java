package org.example.courtsystem.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Base exception for all court-related errors
public class CourtException extends Exception {
    private static final Logger logger = LogManager.getLogger(CourtException.class);

    public CourtException(String message) {
        super(message);
        logger.error("CourtException: {}", message);
    }

    public CourtException(String message, Throwable cause) {
        super(message, cause);
        logger.error("CourtException: {} - Caused by: {}", message, cause.toString());
    }
}