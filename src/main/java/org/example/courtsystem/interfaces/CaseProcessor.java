package org.example.courtsystem.interfaces;

import org.example.courtsystem.model.cases.Case;

public interface CaseProcessor {
    void processCase(Case courtCase);  // Does not declare an exception
}