package org.example.courtsystem.util;

import org.example.courtsystem.annotations.AuthorAnnotation;
import org.example.courtsystem.annotations.CaseMetadata;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Method;

public class AnnotationProcessor {
    private static final Logger logger = LogManager.getLogger(AnnotationProcessor.class);

    public static void processAnnotations(Class<?> clazz) {
        logger.info("\nProcessing annotations for: {}", clazz.getSimpleName());

        // Processing class annotations
        processClassAnnotations(clazz);

        // Processing method annotations
        processMethodAnnotations(clazz);
    }

    private static void processClassAnnotations(Class<?> clazz) {
        // CaseMetadata annotation
        CaseMetadata caseMetadata = clazz.getAnnotation(CaseMetadata.class);
        if (caseMetadata != null) {
            logger.info("Case Metadata:");
            logger.info("  Author: {}", caseMetadata.author());
            logger.info("  Created: {}", caseMetadata.creationDate());
            logger.info("  Version: {}", caseMetadata.version());
        }

        // AuthorAnnotation annotation
        AuthorAnnotation authorAnnotation = clazz.getAnnotation(AuthorAnnotation.class);
        if (authorAnnotation != null) {
            logger.info("Author Info:");
            logger.info("  Author: {}", authorAnnotation.author());
            logger.info("  Date: {}", authorAnnotation.date());
            logger.info("  Description: {}", authorAnnotation.description());
        }
    }

    private static void processMethodAnnotations(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            AuthorAnnotation methodAnnotation = method.getAnnotation(AuthorAnnotation.class);
            if (methodAnnotation != null) {
                logger.info("\nMethod '{}' annotations:", method.getName());
                logger.info("  Author: {}", methodAnnotation.author());
                logger.info("  Date: {}", methodAnnotation.date());
            }
        }
    }
}