package org.example.courtsystem.model.cases;

import org.example.courtsystem.model.EvidenceType;

public record Evidence(String description, EvidenceType type) {
    // Additional checks when creating a record
    public Evidence {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Evidence type cannot be null");
        }
    }
}

// Evidence class represents physical or digital proof for a case
//public class Evidence {
//    private String description;
//    private EvidenceType type;
//
//    public Evidence(String description, EvidenceType type) {
//        this.description = description;
//        this.type = type;
//    }
//
//    // Getters and setters
//    public String getDescription() {
//        return description;
//    }
//
//    public EvidenceType getType() {
//        return type;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setType(EvidenceType type) {
//        this.type = type;
//    }
//}