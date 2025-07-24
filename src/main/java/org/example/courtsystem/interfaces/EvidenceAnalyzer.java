package org.example.courtsystem.interfaces;

import org.example.courtsystem.model.cases.Evidence;

@FunctionalInterface
public interface EvidenceAnalyzer {
    String analyze(Evidence evidence);
}