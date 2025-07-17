package org.example.courtsystem.model.court;

import org.example.courtsystem.model.LegalEntity;
import org.example.courtsystem.model.people.Lawyer;

public class LawFirm extends LegalEntity {
    public LawFirm(String name) {
        super(name);
    }

    public void hireLawyer(Lawyer lawyer) {
        System.out.println("Lawyer " + lawyer.getName() + " hired at " + name);
    }

    // Overrides getType() to return specific type of legal entity
    @Override
    public String getType() {
        return "Law Firm";
    }
}