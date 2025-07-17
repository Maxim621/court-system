package org.example.courtsystem.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Thrown when document processing fails
public class DocumentProcessingException extends CourtException {
    private static final Logger logger = LogManager.getLogger(DocumentProcessingException.class);

    public DocumentProcessingException(String documentTitle, String issue) {
        super("Document processing error for '" + documentTitle + "': " + issue);
        logger.warn("Failed to process document: {} - Reason: {}", documentTitle, issue);
    }
}