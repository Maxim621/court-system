package org.example.courtsystem.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Thrown when invalid evidence is detected
public class InvalidEvidenceException extends CourtException {
    private static final Logger logger = LogManager.getLogger(InvalidEvidenceException.class);
    private final String evidenceDescription;

    public InvalidEvidenceException(String evidenceDescription) {
        super("Invalid evidence: " + evidenceDescription);
        this.evidenceDescription = evidenceDescription;
        logger.warn("Invalid evidence detected: {}", evidenceDescription);
    }

    public InvalidEvidenceException(String evidenceDescription, String reason) {
        super("Invalid evidence '" + evidenceDescription + "': " + reason);
        this.evidenceDescription = evidenceDescription;
        logger.warn("Evidence rejected: {} - Reason: {}", evidenceDescription, reason);
    }

    public String getEvidenceDescription() {
        return evidenceDescription;
    }
}