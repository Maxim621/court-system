package org.example.courtsystem.util;

import org.example.courtsystem.annotations.AuthorAnnotation;
import org.example.courtsystem.annotations.CaseMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationProcessor {
    public static void processAnnotations(Class<?> clazz) {
        System.out.println("\nProcessing annotations for: " + clazz.getSimpleName());

        // Processing class annotations
        processClassAnnotations(clazz);

        // Processing method annotations
        processMethodAnnotations(clazz);
    }

    private static void processClassAnnotations(Class<?> clazz) {
        // CaseMetadata annotation
        CaseMetadata caseMetadata = clazz.getAnnotation(CaseMetadata.class);
        if (caseMetadata != null) {
            System.out.println("Case Metadata:");
            System.out.println("  Author: " + caseMetadata.author());
            System.out.println("  Created: " + caseMetadata.creationDate());
            System.out.println("  Version: " + caseMetadata.version());
        }

        // AuthorAnnotation annotation
        AuthorAnnotation authorAnnotation = clazz.getAnnotation(AuthorAnnotation.class);
        if (authorAnnotation != null) {
            System.out.println("Author Info:");
            System.out.println("  Author: " + authorAnnotation.author());
            System.out.println("  Date: " + authorAnnotation.date());
            System.out.println("  Description: " + authorAnnotation.description());
        }
    }

    private static void processMethodAnnotations(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            AuthorAnnotation methodAnnotation = method.getAnnotation(AuthorAnnotation.class);
            if (methodAnnotation != null) {
                System.out.println("\nMethod '" + method.getName() + "' annotations:");
                System.out.println("  Author: " + methodAnnotation.author());
                System.out.println("  Date: " + methodAnnotation.date());
            }
        }
    }
}