package org.example.courtsystem.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Thrown when lawyer is unavailable for case
public class LawyerUnavailableException extends CourtException {
    private static final Logger logger = LogManager.getLogger(LawyerUnavailableException.class);

    public LawyerUnavailableException(String lawyerName, String reason) {
        super("Lawyer " + lawyerName + " unavailable: " + reason);
        logger.warn("Lawyer unavailable: {} - Reason: {}", lawyerName, reason);
    }
}