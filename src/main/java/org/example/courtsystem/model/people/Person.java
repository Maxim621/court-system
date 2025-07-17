package org.example.courtsystem.model.people;

// Abstract Person: base class for all individuals involved in the legal process (Lawyer, Client, Witness, Judge)
public abstract class Person {
    protected String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String getRole();
}