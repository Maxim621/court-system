package org.example.courtsystem.model.documents;

// LegalDocument represents a general court document
public class LegalDocument extends Document {
    public LegalDocument(String title) {
        super(title);
    }

    // Overrides submit() to provide concrete document submission logic
    @Override
    public void submit() {
        System.out.println("Document \"" + title + "\" submitted to the court.");
    }
}