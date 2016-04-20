package com.excilys.cdb.dto.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.excilys.cdb.dto.implementation.ComputerDTO;

/**
 * Date validator
 * Validate the ComputerDTO class
 * Check whether the introduced date is before the discontinued date
 */
public class ConsistentDateValidator implements ConstraintValidator<ConsistentDate, ComputerDTO> {

	// Date format
	private static final String FORMAT = "yyyy-MM-dd";
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);


	@Override
	public void initialize(ConsistentDate constraintAnnotation) {
	}

	@Override
	public boolean isValid(ComputerDTO value, ConstraintValidatorContext context) {
		
		String date1 = value.getIntroduced();
		String date2 = value.getDiscontinued();
		
		// If a date is null, temporal problem cannot occur
		if (date1 == null || date2 == null) {
			return true;
		}
		// If a date is empty, temporal problem cannot occur
		date1 = date1.trim();
		date2 = date2.trim();
		if (date1.isEmpty() || date2.isEmpty()) {
			return true;
		}
		// The date should be valid
		try {
			LocalDate introduced = LocalDate.parse(date1, FORMATTER);
			LocalDate discontinued = LocalDate.parse(date2, FORMATTER);
			// Introduced should be before discontinued
			if (introduced.isAfter(discontinued)) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("discontinued date should be after introduced date").addConstraintViolation();
				return false;
			}
		} catch(DateTimeParseException e) {
			return false;
		}
		return true;
	}
}