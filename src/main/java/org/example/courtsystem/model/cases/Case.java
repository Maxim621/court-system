package org.example.courtsystem.model.cases;

// Abstract Case: base class for legal proceedings
public abstract class Case {
    protected String title;

    public Case(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public abstract void process();
}