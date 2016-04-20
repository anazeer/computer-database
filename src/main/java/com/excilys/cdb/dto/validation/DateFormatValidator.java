package com.excilys.cdb.dto.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Date validator.
 * Validate a String annotated by @DateFormat that should be a date
 * Also check that the first date is before the second one
 */
public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {
	
	// Date format
	private static final String FORMAT = "yyyy-MM-dd";
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);
	
	@Override
	public void initialize(DateFormat constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		// Check for null (allowed)
		if (value == null) {
			return true;
		}
		// Check for empty (allowed)
		String date = value.trim();
		if (date.isEmpty()) {
			return true;
		}
		// Try to parse the date
		try {
			LocalDate localDate = LocalDate.parse(date, FORMATTER);
			// The date need to be after 1970 to be successfully persisted
			if (localDate.isBefore(LocalDate.of(1970, 01, 02))) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("The date is too old (must be after 1970)").addConstraintViolation();
				return false;
			}
		} catch(DateTimeParseException e) {
			return false;
		}
		return true;
	}	
}