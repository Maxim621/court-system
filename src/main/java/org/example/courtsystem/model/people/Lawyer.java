package org.example.courtsystem.model.people;

import org.example.courtsystem.exceptions.LawyerUnavailableException;
import org.example.courtsystem.interfaces.Defendable;
import org.example.courtsystem.model.cases.ConcreteCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Represents a lawyer who defends clients in court cases.
// Implements Defendable interface for standard defense procedures.
public class Lawyer extends CourtMember implements Defendable {
    private static final Logger logger = LogManager.getLogger(Lawyer.class);
    private int experience;
    private static final int MIN_EXPERIENCE_FOR_COMPLEX_CASES = 5;

    // Creates a new Lawyer instance
    public Lawyer(String name, int experience, int yearsOfService) {
        super(name, yearsOfService);
        if (experience < 0) {
            logger.warn("Negative experience provided, setting to 0");
            this.experience = 0;
        } else {
            this.experience = experience;
        }
        logger.info("New lawyer created: {} ({} years experience)", name, experience);
    }

    // Defends a client in court case
    public void defend(Client client, ConcreteCase courtCase) throws LawyerUnavailableException {
        logger.debug("Attempting to defend client {} in case {}",
                client != null ? client.getName() : "null",
                courtCase != null ? courtCase.getTitle() : "null");

        if (client == null || courtCase == null) {
            logger.error("Null client or case provided");
            throw new IllegalArgumentException("Client and case cannot be null");
        }

        if (this.experience < MIN_EXPERIENCE_FOR_COMPLEX_CASES && courtCase.isComplex()) {
            logger.warn("Lawyer {} lacks experience for complex case", name);
            throw new LawyerUnavailableException(name, "Insufficient experience for complex case");
        }

        logger.info("{} defends {} in case: {}", name, client.getName(), courtCase.getTitle());
    }

    // Sets lawyer's experience
    public void setExperience(int experience) {
        if (experience < 0) {
            logger.warn("Attempt to set negative experience");
            return;
        }
        logger.debug("Updating {}'s experience from {} to {}", name, this.experience, experience);
        this.experience = experience;
    }

    // Gets lawyer's role
    @Override
    public String getRole() {
        return "Lawyer";
    }

    // String representation of lawyer
    @Override
    public String toString() {
        return "Lawyer{name='" + name + "', experience=" + experience + "}";
    }

    // Performs lawyer's primary duty
    @Override
    public void performDuty() {
        logger.info("{} is defending a client.", getName());
    }

    // Prepares defense strategy
    @Override
    public String prepareDefenseStrategy() {
        String strategy = "Strategy: Challenge evidence credibility";
        logger.debug("{} prepared defense strategy: {}", name, strategy);
        return strategy;
    }

    // Cross-examines a witness
    @Override
    public void crossExamineWitness(Witness witness) {
        if (witness == null) {
            logger.error("Attempt to examine null witness");
            throw new IllegalArgumentException("Witness cannot be null");
        }
        logger.info("{} cross-examining witness: {}", name, witness.getName());
    }

    // Compares lawyers for equality
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Lawyer)) return false;
        Lawyer other = (Lawyer) obj;
        return name.equals(other.name) && experience == other.experience;
    }

    // Generates hash code
    @Override
    public int hashCode() {
        return name.hashCode() + 31 * experience;
    }
}