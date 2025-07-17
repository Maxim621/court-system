package org.example.courtsystem.model.people;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

// Represents a person who testifies in court
public class Witness extends Person {
    private final String testimony;
    private static final Set<Integer> usedIds = new HashSet<>();
    private final int witnessId;

    public Witness(String name, String testimony) {
        super(name);
        this.testimony = testimony != null ? testimony : "No testimony provided";
        this.witnessId = generateUniqueId();
    }

    public Witness(String name) {
        this(name, "I saw what happened");
    }

    private int generateUniqueId() {
        if (usedIds.size() >= 1000) {
            throw new IllegalStateException("No more available witness IDs");
        }

        int id;
        do {
            id = ThreadLocalRandom.current().nextInt(1000);
        } while (!usedIds.add(id));

        return id;
    }

    public String provideTestimony() {
        return String.format("Witness #%d %s: \"%s\"",
                witnessId, name, testimony);
    }

    @Override
    public String getRole() {
        return "Witness";
    }

    @Override
    public String toString() {
        return String.format("Witness{id=%d, name='%s'}", witnessId, name);
    }

    public boolean isCredible() {
        return testimony != null &&
                !testimony.isBlank() &&
                !testimony.toLowerCase().contains("i don't remember") &&
                !testimony.toLowerCase().contains("i don't recall");
    }

    // Додаткові геттери
    public String getTestimony() {
        return testimony;
    }

    public int getWitnessId() {
        return witnessId;
    }
}