package org.example.courtsystem.interfaces;

import org.example.courtsystem.model.people.Witness;

// Interface for defense in court
public interface Defendable {
    String prepareDefenseStrategy();
    void crossExamineWitness(Witness witness);
}