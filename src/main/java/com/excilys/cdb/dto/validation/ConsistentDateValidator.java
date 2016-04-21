package com.excilys.cdb.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.excilys.cdb.dto.implementation.ComputerDTO;

/**
 * Date validator
 * Validate the ComputerDTO class
 * Check whether the introduced date is before the discontinued date
 */
public class ConsistentDateValidator implements ConstraintValidator<ConsistentDate, ComputerDTO> {

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
		// If a parse exception occurs, the error message already has been printed by the @DateFormat annotation on the date
		// We just compare the 2 string
		if (date2.compareTo(date1) > 0) {
			return true;
		} else {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{error.date.temporal}").addConstraintViolation();
			return false;
		}
	}
}