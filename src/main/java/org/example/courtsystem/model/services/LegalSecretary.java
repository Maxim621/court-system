package org.example.courtsystem.model.services;

import org.example.courtsystem.exceptions.DocumentProcessingException;
import org.example.courtsystem.interfaces.DocumentDraftable;
import org.example.courtsystem.model.documents.Document;
import org.example.courtsystem.model.documents.LegalDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;

public class LegalSecretary implements DocumentDraftable {
    private static final Logger logger = LogManager.getLogger(LegalSecretary.class);
    private static final String NOTARY_SEAL = "OFFICIAL_SEAL_2023";
    private final Queue<Document> documentQueue = new LinkedList<>();

    // Adds document to processing queue and logs the action
    public void addToQueue(Document doc) throws DocumentProcessingException {
        if (doc == null) {
            throw new DocumentProcessingException("null", "Cannot add null document to queue");
        }
        documentQueue.add(doc);
        logger.info("Added document to queue: {} (Queue size: {})",
                doc.getTitle(), documentQueue.size());
    }

    // Processes the next document in queue
    public Document processNextDocument() throws DocumentProcessingException {
        if (documentQueue.isEmpty()) {
            throw new DocumentProcessingException("Queue empty", "No documents to process");
        }
        Document doc = documentQueue.poll();
        notarizeDocument(doc);
        logger.info("Processed document: {}", doc.getTitle());
        return doc;
    }

    // Drafts a new legal document
    @Override
    public Document draftDocument(String title, String content) throws DocumentProcessingException {
        logger.debug("Attempting to draft document: {}", title);

        if (title == null || title.trim().isEmpty()) {
            logger.error("Document title cannot be empty");
            throw new DocumentProcessingException("Untitled", "Empty title");
        }

        if (content == null || content.trim().isEmpty()) {
            logger.warn("Document content is empty for: {}", title);
            content = "[NO CONTENT]";
        }

        String fullTitle = "[DRAFT] " + title + ": " + content;
        Document doc = new LegalDocument(fullTitle, content);
        addToQueue(doc); // Тепер метод використовується
        return doc;
    }

    // Notarizes a legal document
    @Override
    public void notarizeDocument(Document doc) throws DocumentProcessingException {
        logger.debug("Starting notarization process for: {}",
                doc != null ? doc.getTitle() : "null document");

        if (doc == null) {
            throw new DocumentProcessingException("null", "Null document");
        }

        if (doc.getTitle() == null || doc.getTitle().trim().isEmpty()) {
            logger.warn("Document has empty title during notarization");
        }

        logger.info("Applying notary seal {} to document: {}", NOTARY_SEAL, doc.getTitle());
    }

    // Gets current queue size
    public int getQueueSize() {
        return documentQueue.size();
    }

    // Gets the current notary seal
    public static String getNotarySeal() {
        logger.trace("Accessing notary seal");
        return NOTARY_SEAL;
    }
}