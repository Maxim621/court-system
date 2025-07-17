package org.example.courtsystem.model;

// Abstract LegalEntity: base for legal organizations such as LawFirm and Court
public abstract class LegalEntity {
    protected String name;

    public LegalEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String getType();
}