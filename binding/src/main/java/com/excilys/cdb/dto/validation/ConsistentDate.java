package com.excilys.cdb.dto.validation;

import java.lang.annotation.Documented;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * ComputerDTO annotation
 * Check that introduced date is before discontinued date
 */
@Documented
@Constraint(validatedBy = ConsistentDateValidator.class)
@Target( { ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsistentDate {
 
    String message() default "";
     
    Class<?>[] groups() default {};
     
    Class<? extends Payload>[] payload() default {};
          
}