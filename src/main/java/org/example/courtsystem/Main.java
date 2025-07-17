package org.example.courtsystem;

import org.example.courtsystem.custom.collections.CustomLinkedList;
import org.example.courtsystem.exceptions.*;
import org.example.courtsystem.generics.CaseArchive;
import org.example.courtsystem.generics.EvidenceProcessor;
import org.example.courtsystem.generics.LegalContainer;
import org.example.courtsystem.interfaces.CaseProcessor;
import org.example.courtsystem.model.CaseStatus;
import org.example.courtsystem.model.CourtType;
import org.example.courtsystem.model.EvidenceType;
import org.example.courtsystem.model.cases.ConcreteCase;
import org.example.courtsystem.model.cases.Evidence;
import org.example.courtsystem.model.court.*;
import org.example.courtsystem.model.documents.Document;
import org.example.courtsystem.model.documents.LegalDocument;
import org.example.courtsystem.model.documents.Verdict;
import org.example.courtsystem.model.people.*;
import org.example.courtsystem.model.services.LegalSecretary;
import org.example.courtsystem.util.DocumentAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

// Main class demonstrating the court case simulation with full exception handling
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    // Court system constants
    private static final int MAX_CASE_DURATION_DAYS = 30;
    private static final String COURT_DISTRICT = "SOUTHERN DISTRICT OF NEW YORK";

    public static void main(String[] args) {
        try {
            // Initialize legal system with exception handling
            initializeLegalSystem();
            demonstrateCollections();
            demonstrateGenerics();
        } catch (CourtException e) {
            logger.fatal("Critical system initialization error: {}", e.getMessage());
        }

        try {
            // Example enum usage
            CaseStatus caseStatus = CaseStatus.OPEN;
            logger.info("Case status: {}", caseStatus.getDescription());

            CourtType courtType = CourtType.FEDERAL;
            logger.info("Court type: {}", courtType.getDescription());

            EvidenceType evidenceType = EvidenceType.DOCUMENT;
            logger.info("Evidence type: {}", evidenceType.getDescription());

            // Example document analysis
            DocumentAnalyzer.analyzeDocument("legal_document.txt");
            logger.info("Document analysis completed. Results saved to analysis_results.txt");
        } catch (IOException e) {
            logger.error("Error analyzing document: {}", e.getMessage());
        }
    }

    // Initializes the complete legal system including court, participants and cases.
    private static void initializeLegalSystem() throws CourtException {
        logger.info("INITIALIZING LEGAL SYSTEM");

        try {
            // 1. Create all legal entities
            LawFirm firm = new LawFirm("Pearson Specter Litt");

            // Using lower experience to potentially trigger LawyerUnavailableException
            Lawyer defenseAttorney = new Lawyer("Harvey Specter", 5, 3);
            Lawyer prosecutor = new Lawyer("Cameron Dennis", 20, 18);
            Client defendant = new Client("Mike Ross");
            Judge judge = new Judge("Judith DeLuca", 20);
            Court court = new Court("U.S. District Court", judge);
            LegalSecretary secretary = new LegalSecretary();

            // 2. Display court information
            Court.displayCourtRules();
            logger.info("Jurisdiction: {}", COURT_DISTRICT);

            // 3. Prepare case with full exception handling
            ConcreteCase fraudCase = prepareCase(firm, defenseAttorney, prosecutor,
                    defendant, secretary, court);

            // 4. Conduct trial process
            conductTrialProcess(court, fraudCase, secretary);

            // 5. Demonstrate system features
            demonstrateSystemFeatures(court, fraudCase, defenseAttorney,
                    prosecutor, defendant, judge);

        } catch (LawyerUnavailableException e) {
            logger.error("Lawyer availability issue: {}", e.getMessage());
            throw new CourtException("Case preparation failed due to lawyer issues", e);
        } catch (InvalidEvidenceException e) {
            logger.error("Invalid evidence detected: {}", e.getMessage());
            throw new CourtException("Case processing failed due to evidence issues", e);
        } catch (DocumentProcessingException e) {
            logger.error("Document processing failed: {}", e.getMessage());
            throw new CourtException("Case processing failed due to document issues", e);
        }
    }

    // Prepares a legal case with evidence, witnesses and documents.
    private static ConcreteCase prepareCase(LawFirm firm, Lawyer defenseAttorney,
                                            Lawyer prosecutor, Client defendant,
                                            LegalSecretary secretary, Court court)
            throws LawyerUnavailableException, DocumentProcessingException {

        logger.info("\nCASE PREPARATION");

        // Legal team assembly
        firm.hireLawyer(defenseAttorney);
        logger.info("Prosecutor assigned: {}", prosecutor.getName());

        // Create case
        ConcreteCase fraudCase = new ConcreteCase("U.S. v. Mike Ross", defendant, defenseAttorney);
        logger.info("Case filed: {} ({} days maximum duration)",
                fraudCase.getTitle(), MAX_CASE_DURATION_DAYS);

        // Evidence handling with proper validation
        logger.info("\nEVIDENCE GATHERING");
        List<Evidence> validEvidences = new ArrayList<>();

        // Create evidences with specific types
        Evidence[] evidencesToProcess = {
                new Evidence("Harvard Law records", EvidenceType.DOCUMENT),
                new Evidence("Email correspondence with Jessica Pearson", EvidenceType.DIGITAL),
                new Evidence("Bar Association exam results", EvidenceType.DOCUMENT),
                new Evidence("Signed affidavit", EvidenceType.WITNESS_STATEMENT)
        };

        // Process each piece of evidence
        for (Evidence evidence : evidencesToProcess) {
            try {
                // Explicit validation check
                if (evidence.getDescription().toLowerCase().contains("forged")) {
                    throw new InvalidEvidenceException(
                            evidence.getDescription(),
                            "Forged document detected"
                    );
                }

                validEvidences.add(evidence);
                logger.info("Accepted {} evidence: {}",
                        evidence.getType().getDescription(),
                        evidence.getDescription());

            } catch (InvalidEvidenceException e) {
                logger.warn("Skipping invalid evidence: {} - Reason: {}",
                        e.getEvidenceDescription(), e.getMessage());
            }
        }

        // Add only valid evidences to case
        validEvidences.forEach(fraudCase::addEvidence);

        // Add witnesses
        logger.info("\nWITNESS LIST");
        fraudCase.addWitness(new Witness("Jessica Pearson"));
        fraudCase.addWitness(new Witness("Louis Litt"));

        // Prepare legal documents
        logger.info("\nDOCUMENT PREPARATION");
        Document defenseBrief = secretary.draftDocument(
                "Defense Motion to Suppress",
                "Illegally obtained evidence by prosecution"
        );
        secretary.notarizeDocument(defenseBrief);
        defenseBrief.submit();

        return fraudCase;
    }

    // Conducts the full trial process including verdict and potential appeal.
    private static void conductTrialProcess(Court court, ConcreteCase fraudCase,
                                            LegalSecretary secretary) {
        logger.info("\nTRIAL PROCEEDINGS");

        try {
            // Process case through court system
            court.processCase(fraudCase);

            // Conduct trial and get verdict
            Verdict verdict = court.startTrial(fraudCase);
            verdict.submit();

            // Handle appeal if applicable
            handleAppealProcess(verdict, secretary);

        } catch (CourtException e) {
            logger.error("Trial process failed: {}", e.getMessage());
        }
    }

    // Handles the appeal process if verdict is appealable.
    private static void handleAppealProcess(Verdict verdict, LegalSecretary secretary) {
        logger.info("\nPOST-TRIAL ACTIONS");
        if (verdict.hasGroundsForAppeal()) {
            logger.info("VERDICT APPEALED");
            try {
                verdict.fileAppeal(verdict);

                // Prepare appeal documents with exception handling
                try {
                    Document appealBrief = secretary.draftDocument(
                            "Appeal to Circuit Court",
                            "Grounds: Judicial error in evidence admission"
                    );

                    try {
                        secretary.notarizeDocument(appealBrief);
                        appealBrief.submit();
                    } catch (DocumentProcessingException e) {
                        logger.error("Failed to notarize appeal document: {}", e.getMessage());
                        // Handle notarization failure (e.g., retry or abort)
                    }

                } catch (DocumentProcessingException e) {
                    logger.error("Failed to draft appeal document: {}", e.getMessage());
                    // Handle drafting failure (e.g., use template document)
                }

            } catch (AppealFailedException e) {
                logger.error("Appeal process failed: {}", e.getMessage());
            }
        } else {
            logger.info("Verdict stands - no grounds for appeal");
        }
    }

    // Demonstrates system features including polymorphism and interface usage.
    private static void demonstrateSystemFeatures(Court court, ConcreteCase fraudCase,
                                                  Lawyer defenseAttorney, Lawyer prosecutor,
                                                  Client defendant, Judge judge) {

        logger.info("\nSYSTEM DEMONSTRATION");

        // Polymorphism example - treating all participants as Person objects
        demonstratePolymorphism(defenseAttorney, prosecutor, defendant, judge);

        // Interface demonstration
        demonstrateInterfaceUsage(court, fraudCase);
    }

    // Demonstrates polymorphism by showing participant roles.
    private static void demonstratePolymorphism(Lawyer defenseAttorney, Lawyer prosecutor,
                                                Client defendant, Judge judge) {
        logger.info("\nPARTICIPANT ROLES");
        logger.info(String.format("| %-20s | %-15s |", "Name", "Role"));
        logger.info(String.format("|%s|%s|", "-".repeat(22), "-".repeat(17)));

        Person[] trialParticipants = {
                defenseAttorney,
                prosecutor,
                defendant,
                judge,
                new Witness("Rachel Zane")
        };

        for (Person participant : trialParticipants) {
            logger.info(String.format("| %-20s | %-15s |",
                    participant.getName(),
                    participant.getRole()));
        }
    }

    // Demonstrates interface usage through CaseProcessor.
    private static void demonstrateInterfaceUsage(Court court, ConcreteCase fraudCase) {
        logger.info("\nINTERFACE USAGE");
        CaseProcessor caseProcessor = court;
        caseProcessor.processCase(fraudCase);
    }

    private static void demonstrateCollections() {
        logger.info("\n=== COLLECTIONS DEMONSTRATION ===");

        // ArrayList (already used in prepareCase)
        List<Evidence> evidenceList = new ArrayList<>();
        evidenceList.add(new Evidence("Document A", EvidenceType.DOCUMENT));  // Додано тип
        logger.info("ArrayList size: {}", evidenceList.size());

        // HashSet for unique IDs
        Set<Integer> caseIds = new HashSet<>();
        caseIds.add(101);
        caseIds.add(102);
        caseIds.add(101); // Дублікат не додасться
        logger.info("Unique case IDs: {}", caseIds);

        // HashMap for documents
        Map<String, Document> documentRegistry = new HashMap<>();
        documentRegistry.put("CASE-101", new LegalDocument("Contract"));
        logger.info("Document registry contains key 'CASE-101': {}",
                documentRegistry.containsKey("CASE-101"));

        // LinkedList as a queue
        Queue<Document> documentQueue = new LinkedList<>();
        documentQueue.add(new LegalDocument("Motion to Dismiss"));
        logger.info("Documents in queue: {}", documentQueue.size());

        // TreeSet for sorting
        Set<String> sortedCaseNames = new TreeSet<>();
        sortedCaseNames.add("Zeta vs. State");
        sortedCaseNames.add("Alpha Corp Case");
        logger.info("Sorted case names: {}", sortedCaseNames);
    }

    // Method for demonstrating Generics
    private static void demonstrateGenerics() {
        logger.info("\n=== GENERICS DEMONSTRATION ===");

        // Using own LinkedList
        CustomLinkedList<Witness> witnessList = new CustomLinkedList<>();
        witnessList.add(new Witness("John Doe"));
        witnessList.add(new Witness("Jane Smith"));
        logger.info("First witness: {}", witnessList.get(0).getName());

        // CaseArchive for archiving cases
        CaseArchive<ConcreteCase> caseArchive = new CaseArchive<>();
        ConcreteCase sampleCase = new ConcreteCase("Sample Case", new Client("Test Client"),
                new Lawyer("Test Lawyer", 10, 5));
        caseArchive.archiveCase(sampleCase);
        logger.info("Archived case title: {}", caseArchive.retrieveCase(0).getTitle());

        // EvidenceProcessor for processing evidence
        EvidenceProcessor<Evidence> evidenceProcessor = new EvidenceProcessor<>();
        evidenceProcessor.processEvidence(new Evidence("DNA Sample", EvidenceType.PHYSICAL));  // Added type
        // LegalContainer for object pairing
        LegalContainer<Lawyer, Client> legalPair = new LegalContainer<>(
                new Lawyer("Generic Lawyer", 5, 3),
                new Client("Generic Client")
        );
        logger.info("Legal pair: {} defends {}",
                legalPair.getItem1().getName(),
                legalPair.getItem2().getName());
    }

    // Static initializer block - runs when class is loaded
    static {
        logger.info("Federal Court System Initializing...");
    }
}