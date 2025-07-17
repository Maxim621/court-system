package org.example.courtsystem.generics;

import org.example.courtsystem.model.cases.Evidence;

public class EvidenceProcessor<T extends Evidence> {
    public void processEvidence(T evidence) {
        System.out.println("Processing evidence: " + evidence.getDescription());
    }
}