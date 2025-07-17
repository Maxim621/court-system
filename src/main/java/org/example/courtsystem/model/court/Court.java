package org.example.courtsystem.model.court;

import org.example.courtsystem.exceptions.CourtException;
import org.example.courtsystem.exceptions.InvalidEvidenceException;
import org.example.courtsystem.interfaces.CaseProcessor;
import org.example.courtsystem.interfaces.EvidenceValidator;
import org.example.courtsystem.model.LegalEntity;
import org.example.courtsystem.model.cases.Case;
import org.example.courtsystem.model.cases.ConcreteCase;
import org.example.courtsystem.model.cases.Evidence;
import org.example.courtsystem.model.documents.Document;
import org.example.courtsystem.model.documents.Verdict;
import org.example.courtsystem.model.people.Judge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

// Represents a court where cases are judged
public class Court extends LegalEntity implements CaseProcessor {
    private static final Logger logger = LogManager.getLogger(Court.class);
    private final Judge judge;
    private static final int MAX_EVIDENCE_ITEMS = 100;
    private final Map<String, List<Document>> caseDocuments = new HashMap<>();

    // Added a method for retrieving case documents
    public List<Document> getCaseDocuments(String caseId) {
        return caseDocuments.getOrDefault(caseId, Collections.emptyList());
    }

    // Adds document to case file
    public void addDocument(String caseId, Document doc) throws CourtException {
        if (caseId == null || caseId.isBlank()) {
            throw new CourtException("Case ID cannot be null or empty");
        }
        if (doc == null) {
            throw new CourtException("Document cannot be null");
        }

        caseDocuments.computeIfAbsent(caseId, k -> new ArrayList<>()).add(doc);
        logger.info("Document added to case {}: {}", caseId, doc.getTitle());
    }

    // Creates new Court instance
    public Court(String name, Judge judge) throws CourtException {
        super(name);
        if (judge == null) {
            logger.error("Attempt to create court without judge");
            throw new CourtException("Court must have a presiding judge");
        }
        this.judge = judge;
        logger.info("Court '{}' initialized with Judge {}", name, judge.getName());
    }

    public static void displayCourtRules() {
        logger.info("\n=== COURT RULES ===");
        logger.info("1. No cell phones in courtroom");
        logger.info("2. Proper attire required");
    }

    // Method for reviewing evidence before a hearing
    public void preTrialEvidenceReview(List<Evidence> evidenceList) throws InvalidEvidenceException {
        if (evidenceList == null) {
            throw new InvalidEvidenceException("null", "Evidence list cannot be null");
        }

        if (evidenceList.size() > MAX_EVIDENCE_ITEMS) {
            throw new InvalidEvidenceException("Evidence limit exceeded",
                    "Maximum allowed: " + MAX_EVIDENCE_ITEMS);
        }

        for (Evidence evidence : evidenceList) {
            validateEvidenceItem(evidence);
        }
    }

    private void validateEvidenceItem(Evidence evidence) throws InvalidEvidenceException {
        if (evidence == null) {
            throw new InvalidEvidenceException("null", "Null evidence in list");
        }

        String description = evidence.getDescription();
        if (description == null || description.isEmpty()) {
            throw new InvalidEvidenceException(description, "Empty evidence description");
        }

        if (description.toLowerCase().contains("підробка")) {
            throw new InvalidEvidenceException(description, "Forged document detected");
        }
    }

    public void validateAllEvidence(EvidenceValidator validator, List<Evidence> evidence) {
        evidence.forEach(e -> {
            try {
                if (!validator.validateEvidence(e)) {
                    logger.warn("Evidence rejected by validator: {}", e.getDescription());
                }
            } catch (InvalidEvidenceException ex) {
                logger.error("Evidence validation failed: {}", ex.getMessage());
            }
        });
    }

    private void conductOpeningStatements(ConcreteCase courtCase) throws CourtException {
        if (courtCase == null || courtCase.getLawyer() == null || courtCase.getClient() == null) {
            throw new CourtException("Invalid case setup for opening statements");
        }

        logger.info("\n--- Opening Statements ---");

        try {
            // Prosecution statement
            logger.info("Prosecutor: \"Your Honor, we will prove {} committed securities fraud\"",
                    courtCase.getClient().getName());

            // Defense statement
            logger.info("Defense ({}): \"My client is innocent of these baseless accusations\"",
                    courtCase.getLawyer().getName());

        } catch (Exception e) {
            throw new CourtException("Failed to deliver opening statements", e);
        }
    }

    // Starts the trial and returns the verdict
    public Verdict startTrial(ConcreteCase courtCase) throws CourtException {
        if (courtCase == null) {
            logger.error("Attempt to start trial with null case");
            throw new CourtException("Case cannot be null");
        }

        // Added logging of case documents
        List<Document> docs = getCaseDocuments(courtCase.getTitle());
        if (!docs.isEmpty()) {
            logger.info("Case documents: {}", docs.stream()
                    .map(Document::getTitle)
                    .collect(Collectors.joining(", ")));
        }

        logger.info("\n=== COURT SESSION BEGINS ===");
        logger.info("Judge {} presiding over {} case", judge.getName(), courtCase.getTitle());

        try {
            conductOpeningStatements(courtCase);
            conductWitnessExamination(courtCase);
            presentEvidence(courtCase);
            conductClosingArguments(courtCase);
        } catch (InvalidEvidenceException e) {
            logger.error("Trial procedure failed due to invalid evidence", e);
            throw new CourtException("Trial cannot proceed with invalid evidence", e);
        }

        return deliverVerdict(courtCase);
    }

    private void conductWitnessExamination(ConcreteCase courtCase) {
        logger.info("\n--- Witness Testimony ---");
        courtCase.getWitnesses().forEach(witness -> {
            logger.info("Examining witness: {}", witness.getName());
            logger.info("Prosecutor: \"Where were you on the night of January 15th?\"");
            logger.info("{}: \"I was reviewing the financial documents\"", witness.getName());
            logger.info("Defense: \"Objection! Leading question!\"");
            logger.info("Judge: \"Sustained. Rephrase your question\"");
        });
    }

    private void presentEvidence(ConcreteCase courtCase) throws CourtException {
        try {
            preTrialEvidenceReview(courtCase.getEvidenceList());
            validateAllEvidence(judge, courtCase.getEvidenceList());
        } catch (InvalidEvidenceException e) {
            throw new CourtException("Evidence validation failed", e);
        }
    }

    private void conductClosingArguments(ConcreteCase courtCase) {
        logger.info("\n--- Closing Arguments ---");
        logger.info("Prosecutor: \"The evidence clearly shows willful violation of SEC rules\"");
        logger.info("Defense ({}): \"The prosecution failed to meet burden of proof\"",
                courtCase.getLawyer().getName());
    }

    private Verdict deliverVerdict(ConcreteCase courtCase) {
        logger.info("\n--- Judge's Decision ---");
        boolean guilty;
        try {
            guilty = !judge.considerEvidence(
                    courtCase.getEvidenceList(),
                    courtCase.getWitnesses()
            );
        } catch (InvalidEvidenceException e) {
            logger.error("Evidence consideration error", e);
            guilty = false;
        }

        String result = guilty ? "GUILTY" : "NOT GUILTY";
        logger.info("Judge {}: \"I find the defendant {}\"", judge.getName(), result);
        return new Verdict(result);
    }

    @Override
    public void processCase(Case courtCase) {
        Objects.requireNonNull(courtCase, "Case cannot be null");
        courtCase.process();
        logger.info("Processed case: {}", courtCase.getTitle());
    }

    // Overrides abstract method to specify type of legal entity
    @Override
    public String getType() {
        return "Court";
    }

    // Overrides toString() to provide a readable summary of the court
    @Override
    public String toString() {
        return "Court{name='" + name + "', judge='" + judge.getName() + "'}";
    }

    public Judge getJudge() {
        return this.judge;
    }
}