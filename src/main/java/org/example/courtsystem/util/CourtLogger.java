// Singleton class for logging court events
package org.example.courtsystem.util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class CourtLogger {
    private static volatile CourtLogger instance;
    private final Logger logger;

    private CourtLogger() {
        this.logger = LogManager.getLogger(CourtLogger.class);
    }

    public static CourtLogger getInstance() {
        if (instance == null) {
            synchronized (CourtLogger.class) {
                if (instance == null) {
                    instance = new CourtLogger();
                }
            }
        }
        return instance;
    }

    public void logEvent(String message) {
        logger.info("[COURT EVENT] " + message);
    }

    public void logError(String error) {
        logger.error("[COURT ERROR] " + error);
    }
}
