package org.example.courtsystem.interfaces;

import org.example.courtsystem.exceptions.AppealFailedException;
import org.example.courtsystem.model.documents.Verdict;

// Appeals interface
public interface Appealable {
    void fileAppeal(Verdict verdict) throws AppealFailedException;
    boolean hasGroundsForAppeal();
}