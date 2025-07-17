package org.example.courtsystem.model;

public enum CourtType {
    DISTRICT("District Court"),
    FEDERAL("Federal Court"),
    SUPREME("Supreme Court"),
    APPEALS("Court of Appeals");

    private final String description;

    CourtType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
