package org.example.courtsystem.model.cases;

import org.example.courtsystem.model.EvidenceType;

// Evidence class represents physical or digital proof for a case
public class Evidence {
    private String description;
    private EvidenceType type;

    public Evidence(String description, EvidenceType type) {
        this.description = description;
        this.type = type;
    }

    // Getters and setters
    public String getDescription() {
        return description;
    }

    public EvidenceType getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(EvidenceType type) {
        this.type = type;
    }
}