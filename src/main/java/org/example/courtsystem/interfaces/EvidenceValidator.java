package org.example.courtsystem.interfaces;

import org.example.courtsystem.exceptions.InvalidEvidenceException;
import org.example.courtsystem.model.cases.Evidence;

// Interface for checking evidence
public interface EvidenceValidator {
    boolean validateEvidence(Evidence evidence) throws InvalidEvidenceException;
}
