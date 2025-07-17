package org.example.courtsystem.generics;

import org.example.courtsystem.model.cases.Case;

import java.util.ArrayList;
import java.util.List;

public class CaseArchive<T extends Case> {
    private final List<T> archivedCases = new ArrayList<>();

    public void archiveCase(T caseToArchive) {
        archivedCases.add(caseToArchive);
    }

    public T retrieveCase(int index) {
        return archivedCases.get(index);
    }
}