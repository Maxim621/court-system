package org.example.courtsystem.interfaces;

import org.example.courtsystem.exceptions.DocumentProcessingException;
import org.example.courtsystem.model.documents.Document;

// Interface for creating documents
public interface DocumentDraftable {
    Document draftDocument(String title, String content) throws DocumentProcessingException;
    void notarizeDocument(Document doc) throws DocumentProcessingException;
}