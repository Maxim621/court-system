package org.example.courtsystem.model.people;

// Client class represents the person being defended in court
public class Client extends Person {
    public Client(String name) {
        super(name);
    }

    // Overrides getRole() to return the role of this person
    @Override
    public String getRole() {
        return "Client";
    }

    // Overrides toString() to return a readable representation of the client
    @Override
    public String toString() {
        return "Client{name='" + name + "'}";
    }

    // Overrides equals() to compare clients by their names
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Client)) return false;
        Client other = (Client) obj;
        return name.equals(other.name);
    }

    // Overrides hashCode() to match equals() based on name
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}