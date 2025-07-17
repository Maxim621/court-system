package org.example.courtsystem.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Thrown when appeal process fails
public class AppealFailedException extends org.example.courtsystem.exceptions.CourtException {
    private static final Logger logger = LogManager.getLogger(AppealFailedException.class);

    public AppealFailedException(String caseTitle, String reason) {
        super("Appeal failed for case '" + caseTitle + "': " + reason);
        logger.error("Appeal failure: {} - Reason: {}", caseTitle, reason);
    }
}