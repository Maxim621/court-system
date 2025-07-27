package org.example.courtsystem.model.cases;

import org.example.courtsystem.annotations.CaseMetadata;
import org.example.courtsystem.model.people.Client;
import org.example.courtsystem.model.people.Lawyer;
import org.example.courtsystem.model.people.Witness;

import java.util.ArrayList;
import java.util.List;

@CaseMetadata(
        author = "LegalSystemTeam",
        creationDate = "2025-07-27",
        version = 2
)

// ConcreteCase represents a legal dispute between a client and the court
public class ConcreteCase extends Case {
    private final Client client;
    private Lawyer lawyer;
    private final List<Evidence> evidenceList = new ArrayList<>();
    private final List<Witness> witnesses = new ArrayList<>();
    private boolean isComplex;

    public ConcreteCase(String title, Client client, Lawyer lawyer) {
        super(title);
        this.client = client;
        this.lawyer = lawyer;
    }

    public boolean isComplex() {
        return isComplex;
    }

    public void setComplex(boolean complex) {
        isComplex = complex;
    }

    public void addEvidence(Evidence e) {
        evidenceList.add(e);
    }

    public void addWitness(Witness w) {
        witnesses.add(w);
    }

    public Client getClient() {
        return client;
    }

    public Lawyer getLawyer() {
        return lawyer;
    }

    public void setLawyer(Lawyer lawyer) {
        this.lawyer = lawyer;
    }

    public List<Evidence> getEvidenceList() {
        return new ArrayList<>(evidenceList); // Return defensive copy
    }

    public List<Witness> getWitnesses() {
        return new ArrayList<>(witnesses); // Return defensive copy
    }

    @Override
    public void process() {
        System.out.println("Processing case: " + title);
    }

    @Override
    public String toString() {
        return "ConcreteCase{title='" + title + "', client='" + client.getName() + "'}";
    }
}