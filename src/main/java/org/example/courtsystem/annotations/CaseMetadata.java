package org.example.courtsystem.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CaseMetadata {
    String author();
    String creationDate();
    String lastModified() default "";
    int version() default 1;
}