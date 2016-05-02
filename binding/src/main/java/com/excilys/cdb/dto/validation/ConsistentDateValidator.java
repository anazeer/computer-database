package com.excilys.cdb.dto.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.excilys.cdb.dto.implementation.ComputerDTO;

/**
 * Date validator
 * Validate the ComputerDTO class
 * Check whether the introduced date is before the discontinued date
 */
public class ConsistentDateValidator implements ConstraintValidator<ConsistentDate, ComputerDTO> {
	
	@Autowired
	private MessageSource messageSource;

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
		// We compare the 2 dates
		String pattern = messageSource.getMessage("util.date.format", null, LocaleContextHolder.getLocale());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		try {
		LocalDate introduced = LocalDate.parse(date1, formatter);
		LocalDate discontinued = LocalDate.parse(date2, formatter);
		if (introduced.isBefore(discontinued)) {
			return true;
		} else {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{error.date.temporal}").addConstraintViolation();
			return false;
		}
		} catch(DateTimeParseException e) {
			// If a parse exception occurs, the error message already has been printed by the @DateFormat annotation on the date
			return false;
		}

	}
}