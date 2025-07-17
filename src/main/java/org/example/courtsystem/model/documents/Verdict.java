package org.example.courtsystem.model.documents;

import org.example.courtsystem.exceptions.AppealFailedException;
import org.example.courtsystem.interfaces.Appealable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Represents the final verdict in a legal case with appeal functionality.
// Extends Document and implements Appealable interface.
public class Verdict extends Document implements Appealable {
    private static final Logger logger = LogManager.getLogger(Verdict.class);

    private final String result;
    private final LocalDate date;
    private static final int APPEAL_DEADLINE_DAYS = 30;
    private static final String[] APPEALABLE_RESULTS = {"GUILTY", "ERROR", "MISTRIAL"};

    // Creates a new Verdict instance
    public Verdict(String result) {
        super("VERDICT_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE));

        if (result == null || result.trim().isEmpty()) {
            logger.error("Attempt to create verdict with empty result");
            throw new IllegalArgumentException("Verdict result cannot be empty");
        }

        this.result = result.trim().toUpperCase();
        this.date = LocalDate.now();
        logger.info("New verdict created: {} - {}", this.title, this.result);
    }

    // Submits verdict to court records
    @Override
    public void submit() {
        logger.info("Official verdict submitted to court records: {}", this.title);
        logger.info("Verdict result: {}", this.result);
    }

    // Gets the verdict result
    public String getResult() {
        return result;
    }

    // Calculates appeal deadline
    public LocalDate getAppealDeadline() {
        LocalDate deadline = date.plusDays(APPEAL_DEADLINE_DAYS);
        logger.debug("Appeal deadline for {}: {}", title, deadline);
        return deadline;
    }

    // Initiates appeal process
    @Override
    public void fileAppeal(Verdict verdict) throws AppealFailedException {
        logger.info("\n=== APPEAL PROCESS INITIATED ===");

        if (!hasGroundsForAppeal()) {
            logger.error("No legal grounds for appeal");
            throw new AppealFailedException(title, "No legal grounds for appeal");
        }

        if (LocalDate.now().isAfter(getAppealDeadline())) {
            logger.error("Appeal deadline passed");
            throw new AppealFailedException(title, "Appeal deadline passed");
        }

        logger.info("Verdict: {}", verdict);
        logger.info("Appeal must be filed by: {}", getAppealDeadline());
        logger.debug("Preparing appellate brief...");
        logger.debug("Contacting appeals court clerk...");
    }

    // Checks if verdict can be appealed
    @Override
    public boolean hasGroundsForAppeal() {
        for (String appealableResult : APPEALABLE_RESULTS) {
            if (result.equals(appealableResult)) {
                logger.debug("Appeal grounds found for verdict: {}", result);
                return true;
            }
        }
        logger.debug("No appeal grounds for verdict: {}", result);
        return false;
    }

    // String representation of verdict
    @Override
    public String toString() {
        return String.format("Verdict{result='%s', date=%s, caseNo='%s'}",
                result, date, title);
    }
}