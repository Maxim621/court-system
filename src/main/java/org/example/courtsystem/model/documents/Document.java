package org.example.courtsystem.model.documents;

// Abstract Document: represents any legal document (e.g., LegalDocument, Verdict)
public abstract class Document {
    protected String title;

    public Document(String title) {
        this.title = title;
    }

    // Add getter for title
    public String getTitle() {
        return title;
    }

    public abstract void submit();
}