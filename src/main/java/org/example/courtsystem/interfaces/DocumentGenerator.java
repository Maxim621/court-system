package org.example.courtsystem.interfaces;

import org.example.courtsystem.model.documents.Document;

@FunctionalInterface
public interface DocumentGenerator {
    Document generate(String title, String content);
}
