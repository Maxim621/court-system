package org.example.courtsystem.interfaces;

import org.example.courtsystem.model.cases.Case;

@FunctionalInterface
public interface CaseFilter {
    boolean test(Case courtCase);
}
