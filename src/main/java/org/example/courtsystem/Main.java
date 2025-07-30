package org.example.courtsystem;

import org.example.courtsystem.annotations.AuthorAnnotation;
import org.example.courtsystem.custom.collections.CustomLinkedList;
import org.example.courtsystem.exceptions.*;
import org.example.courtsystem.generics.CaseArchive;
import org.example.courtsystem.generics.EvidenceProcessor;
import org.example.courtsystem.generics.LegalPair;
import org.example.courtsystem.interfaces.*;
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
import org.example.courtsystem.util.AnnotationProcessor;
import org.example.courtsystem.util.DocumentAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.courtsystem.threads.CaseProcessorThread;
import org.example.courtsystem.util.CourtLogger;

import java.io.IOException;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AuthorAnnotation(
        author = "CourtSystemTeam",
        date = "2025-07-27",
        description = "Main application class for court system simulation"
)
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
            demonstrateAnnotations();
            demonstrateThreadsAndSingleton();
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

    private static void demonstrateAnnotations() {
        try {
            logger.info("\n=== ANNOTATION DEMONSTRATION ===");

            // Demonstration of class annotations
            AnnotationProcessor.processAnnotations(ConcreteCase.class);

            // Demonstration of annotations for the Main class itself
            AnnotationProcessor.processAnnotations(Main.class);

            // Demonstration of working with record
            Evidence sampleEvidence = new Evidence("Test Record", EvidenceType.DOCUMENT);
            logger.info("Evidence record created: {}", sampleEvidence);

        } catch (Exception e) {
            logger.error("Annotation processing failed: {}", e.getMessage());
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
        firm.hireLawyer(defenseAttorney);
        logger.info("Prosecutor assigned: {}", prosecutor.getName());

        ConcreteCase fraudCase = new ConcreteCase("U.S. v. Mike Ross", defendant, defenseAttorney);
        logger.info("Case filed: {} ({} days maximum duration)",
                fraudCase.getTitle(), MAX_CASE_DURATION_DAYS);

        logger.info("\nEVIDENCE GATHERING");
        Evidence[] evidencesToProcess = {
                new Evidence("Harvard Law records", EvidenceType.DOCUMENT),
                new Evidence("Email correspondence with Jessica Pearson", EvidenceType.DIGITAL),
                new Evidence("Bar Association exam results", EvidenceType.DOCUMENT),
                new Evidence("Signed affidavit", EvidenceType.WITNESS_STATEMENT)
        };

        // Replacing a loop with a Stream API
        List<Evidence> validEvidences = Stream.of(evidencesToProcess)
                .peek(evidence -> logger.info("Processing evidence: {}", evidence.description()))
                .filter(evidence -> {
                    try {
                        if (evidence.description().toLowerCase().contains("forged")) {
                            throw new InvalidEvidenceException(
                                    evidence.description(),
                                    "Forged document detected"
                            );
                        }
                        return true;
                    } catch (InvalidEvidenceException e) {
                        logger.warn("Skipping invalid evidence: {} - Reason: {}",
                                e.getEvidenceDescription(), e.getMessage());
                        return false;
                    }
                })
                .collect(Collectors.toList());

        validEvidences.forEach(fraudCase::addEvidence);

        logger.info("\nWITNESS LIST");
        // Adding witnesses via Stream
        Stream.of("Jessica Pearson", "Louis Litt")
                .map(Witness::new)
                .peek(witness -> logger.info("Adding witness: {}", witness.getName()))
                .forEach(fraudCase::addWitness);

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
            // Process case through court system using functional interface
            CaseProcessor caseProcessor = court::processCase;
            caseProcessor.processCase(fraudCase);

            // Conduct trial and get verdict
            Verdict verdict = court.startTrial(fraudCase);
            verdict.submit();

            // Handle appeal if applicable
            if (verdict.hasGroundsForAppeal()) {
                // Create an Appealable implementation
                Appealable appealHandler = new Appealable() {
                    @Override
                    public void fileAppeal(Verdict v) throws AppealFailedException {
                        v.fileAppeal(v);  // Assuming Verdict has this method
                    }

                    @Override
                    public boolean hasGroundsForAppeal() {
                        return verdict.hasGroundsForAppeal();
                    }
                };

                try {
                    appealHandler.fileAppeal(verdict);
                    handleAppealProcess(verdict, secretary);
                } catch (AppealFailedException e) {
                    logger.error("Appeal process failed: {}", e.getMessage());
                }
            }

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

    private static void demonstrateFunctionalInterfaces(Court court, ConcreteCase fraudCase) {
        logger.info("\n=== FUNCTIONAL INTERFACES DEMONSTRATION ===");

        // Using CaseFilter
        CaseFilter criminalCaseFilter = c -> c.getTitle().contains("Criminal");
        logger.info("Is fraud case criminal? {}", criminalCaseFilter.test(fraudCase));

        // Using DocumentGenerator
        DocumentGenerator briefGenerator = (title, content) -> {
            logger.info("Generating document: {}", title);
            return new LegalDocument(title, content);
        };

        Document motion = briefGenerator.generate("Motion to Dismiss", "Defense arguments...");
        logger.info("Document created: {}", motion.getTitle());

        // Using EvidenceAnalyzer - use getEvidenceList() instead of getEvidences()
        EvidenceAnalyzer evidenceAnalyzer = evidence -> {
            String analysis = String.format("%s evidence: %s",
                    evidence.type(),
                    evidence.description());
            logger.info(analysis);
            return analysis;
        };

        fraudCase.getEvidenceList().forEach(evidenceAnalyzer::analyze);
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

        demonstrateFunctionalInterfaces(court, fraudCase);
    }

    // Demonstrates polymorphism by showing participant roles.
    private static void demonstratePolymorphism(Lawyer defenseAttorney, Lawyer prosecutor,
                                                Client defendant, Judge judge) {
        logger.info("\nPARTICIPANT ROLES");
        logger.info(String.format("| %-20s | %-15s |", "Name", "Role"));
        logger.info(String.format("|%s|%s|", "-".repeat(22), "-".repeat(17)));

        // Using Stream to display participant information
        Stream.of(
                defenseAttorney,
                prosecutor,
                defendant,
                judge,
                new Witness("Rachel Zane")
        ).forEach(participant -> logger.info(String.format("| %-20s | %-15s |",
                participant.getName(),
                participant.getRole())));
    }

    // Demonstrates interface usage through CaseProcessor.
    private static void demonstrateInterfaceUsage(Court court, ConcreteCase fraudCase) {
        logger.info("\nINTERFACE USAGE");
        CaseProcessor caseProcessor = court;
        caseProcessor.processCase(fraudCase);
    }

    private static void demonstrateCollections() {
        logger.info("\n=== COLLECTIONS DEMONSTRATION (WITH STREAM API) ===");

        // Define sample content for documents
        String content = "Sample document content";

        // ArrayList with Stream
        List<Evidence> evidenceList = Stream.of(
                new Evidence("Document A", EvidenceType.DOCUMENT),
                new Evidence("Photo B", EvidenceType.PHYSICAL)
        ).collect(Collectors.toList());
        logger.info("ArrayList size: {}", evidenceList.size());

        // HashSet with Stream
        Set<Integer> caseIds = Stream.of(101, 102, 101)
                .collect(Collectors.toSet());
        logger.info("Unique case IDs: {}", caseIds);

        // HashMap with Stream
        Map<String, Document> documentRegistry = Stream.of(
                new AbstractMap.SimpleEntry<>("CASE-101", new LegalDocument("Contract", content)),
                new AbstractMap.SimpleEntry<>("CASE-102", new LegalDocument("Affidavit", content))
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        logger.info("Document registry contains key 'CASE-101': {}",
                documentRegistry.containsKey("CASE-101"));

        // LinkedList as a queue with Stream
        Queue<Document> documentQueue = Stream.of(
                        new LegalDocument("Motion to Dismiss", content))
                .collect(Collectors.toCollection(LinkedList::new));
        logger.info("Documents in queue: {}", documentQueue.size());

        // TreeSet with Stream
        Set<String> sortedCaseNames = Stream.of("Zeta vs. State", "Alpha Corp Case")
                .collect(Collectors.toCollection(TreeSet::new));
        logger.info("Sorted case names: {}", sortedCaseNames);
    }

    // Method for demonstrating Generics
    private static void demonstrateGenerics() {
        logger.info("\n=== GENERICS DEMONSTRATION (WITH LAMBDAS) ===");

        // Using lambda to create witnesses
        CustomLinkedList<Witness> witnessList = new CustomLinkedList<>();
        Stream.of("John Doe", "Jane Smith")
                .map(Witness::new)
                .forEach(witnessList::add);
        logger.info("First witness: {}", witnessList.get(0).getName());

        // Using Lambda for archiving cases
        CaseArchive<ConcreteCase> caseArchive = new CaseArchive<>();
        Stream.of(
                new ConcreteCase("Sample Case 1", new Client("Client A"),
                        new Lawyer("Lawyer X", 10, 5)),
                new ConcreteCase("Sample Case 2", new Client("Client B"),
                        new Lawyer("Lawyer Y", 8, 4))
        ).forEach(caseArchive::archiveCase);
        logger.info("Archived case title: {}", caseArchive.retrieveCase(0).getTitle());

        // Using lambdas for proof processing
        EvidenceProcessor<Evidence> evidenceProcessor = new EvidenceProcessor<>();
        Stream.of(
                new Evidence("DNA Sample", EvidenceType.PHYSICAL),
                new Evidence("Email", EvidenceType.DIGITAL)
        ).forEach(evidenceProcessor::processEvidence);

        // Using lambda to create legal pairs
        LegalPair<Lawyer, Client> legalPair = new LegalPair<>(
                new Lawyer("Generic Lawyer", 5, 3),
                new Client("Generic Client")
        );
        logger.info("Legal pair: {} defends {}",
                legalPair.lawyer().getName(),
                legalPair.client().getName());
    }

    private static void demonstrateThreadsAndSingleton() {
        CourtLogger logger = CourtLogger.getInstance();
        logger.logEvent("Starting multi-threaded case processing demonstration");

        // Create several cases for processing
        ConcreteCase[] cases = {
                new ConcreteCase("State vs. Johnson", new Client("Mike Johnson"),
                        new Lawyer("Sarah Connor", 10, 8)),
                new ConcreteCase("State vs. Smith", new Client("John Smith"),
                        new Lawyer("Bob Builder", 15, 12)),
                new ConcreteCase("State vs. Williams", new Client("Alice Williams"),
                        new Lawyer("Lisa Ray", 8, 5))
        };

        // Creating a thread pool
        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            // Start processing each case in a separate thread
            for (ConcreteCase courtCase : cases) {
                executor.execute(new CaseProcessorThread(courtCase));
            }
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        logger.logEvent("All cases submitted for processing");
    }

    // Static initializer block - runs when class is loaded
    static {
        logger.info("Federal Court System Initializing...");
    }
}