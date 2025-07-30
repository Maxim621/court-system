package org.example.courtsystem.threads;

import org.example.courtsystem.util.CourtLogger;
import org.example.courtsystem.model.cases.ConcreteCase;

public class CaseProcessorThread implements Runnable {
    private final ConcreteCase courtCase;
    private final CourtLogger logger;

    public CaseProcessorThread(ConcreteCase courtCase) {
        this.courtCase = courtCase;
        this.logger = CourtLogger.getInstance();
    }

    @Override
    public void run() {
        try {
            logger.logEvent("Processing case: " + courtCase.getTitle() + " in thread: "
                    + Thread.currentThread().getName());

            // Case processing simulation
            Thread.sleep(1000);

            logger.logEvent("Case processed: " + courtCase.getTitle());
        } catch (InterruptedException e) {
            logger.logError("Thread interrupted for case: " + courtCase.getTitle());
            Thread.currentThread().interrupt();
        }
    }
}