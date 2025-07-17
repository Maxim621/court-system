package org.example.courtsystem.model.people;

import org.example.courtsystem.exceptions.InvalidEvidenceException;
import org.example.courtsystem.interfaces.EvidenceValidator;
import org.example.courtsystem.model.cases.Evidence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

// Represents a judge who decides the outcome
public class Judge extends CourtMember implements EvidenceValidator {
    private static final Logger logger = LogManager.getLogger(Judge.class);

    // Sorted collection of evidence maintained by the judge
    private final SortedSet<Evidence> sortedEvidence = new TreeSet<>(
            Comparator.comparing(Evidence::getDescription)
    );

    // Retrieves a copy of the judge's sorted evidence collection
    public SortedSet<Evidence> getSortedEvidence() {
        return new TreeSet<>(sortedEvidence); // Return defensive copy
    }

    // Adds evidence to judge's collection after validation
    public void addEvidence(Evidence evidence) throws InvalidEvidenceException {
        validateEvidence(evidence); // Will throw exception if invalid
        sortedEvidence.add(evidence);
        logger.debug("Judge {} added evidence: {}", name, evidence.getDescription());
    }

    // Creates a new Judge instance
    public Judge(String name, int yearsOfService) {
        super(name, yearsOfService);
        logger.info("New judge appointed: {} ({} years experience)", name, yearsOfService);
    }

    // Makes a legal decision based on evidence
    public String makeDecision(boolean enoughEvidence) {
        logger.debug("Judge {} deliberating...", name);
        String verdict = enoughEvidence ? "GUILTY" : "NOT GUILTY";
        logger.info("Judge {}'s verdict: {}", name, verdict);
        return verdict;
    }

    // Evaluates the credibility of evidence and witnesses
    public boolean considerEvidence(List<Evidence> evidence, List<Witness> witnesses)
            throws InvalidEvidenceException {
        // Validate inputs
        if (evidence == null || witnesses == null) {
            logger.error("Invalid input: evidence or witnesses list is null");
            throw new IllegalArgumentException("Evidence and witnesses cannot be null");
        }

        logger.info("Judge {} reviewing {} evidence items and {} witnesses...",
                name, evidence.size(), witnesses.size());

        // Process and validate all evidence
        for (Evidence ev : evidence) {
            addEvidence(ev); // Validates and adds to sorted collection
        }

        // Calculate credibility score
        boolean isCredible = evidence.size() >= 2 && witnesses.size() >= 1;
        double randomFactor = Math.random() * 0.3; // Simulates judicial discretion
        boolean finalDecision = isCredible && (0.7 + randomFactor) > 0.8;

        logger.info("Evidence credibility assessment: {}", finalDecision);
        return finalDecision;
    }

    // Overrides getRole() method to return the role of this person
    @Override
    public String getRole() {
        return "Judge";
    }

    @Override
    public void performDuty() {
        logger.info("{} is presiding over the case.", getName());
    }

    // Verification logic (e.g., whether the document is forged)
    @Override
    public boolean validateEvidence(Evidence evidence) throws InvalidEvidenceException {
        if (evidence == null) {
            logger.error("Null evidence provided for validation");
            throw new InvalidEvidenceException("null");
        }

        String description = evidence.getDescription();
        if (description == null || description.trim().isEmpty()) {
            logger.warn("Empty evidence description");
            throw new InvalidEvidenceException("Empty description");
        }

        if (description.toLowerCase().contains("підробка")) {
            logger.warn("Forged evidence detected: {}", description);
            throw new InvalidEvidenceException(description);
        }

        logger.debug("Evidence validated successfully: {}", description);
        return true;
    }
}