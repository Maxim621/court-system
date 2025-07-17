package org.example.courtsystem.model;

public enum EvidenceType {
    DOCUMENT("Written evidence"),
    PHYSICAL("Physical object"),
    DIGITAL("Digital file"),
    WITNESS_STATEMENT("Witness testimony");

    private final String description;

    EvidenceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
