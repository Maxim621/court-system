package org.example.courtsystem.model;

public enum CaseStatus {
    OPEN("Open for submissions"),
    IN_PROGRESS("Under review"),
    CLOSED("Completed"),
    APPEALED("Under appeal");

    private final String description;

    CaseStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
