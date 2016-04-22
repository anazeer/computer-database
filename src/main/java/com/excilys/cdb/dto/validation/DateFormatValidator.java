package com.excilys.cdb.dto.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Date validator.
 * Validate a String annotated by @DateFormat that should be a date
 * Also check that the first date is before the second one
 */
public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {
	
	@Autowired
	private MessageSource messageSource;

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
			String pattern = messageSource.getMessage("util.date.format", null, LocaleContextHolder.getLocale());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			LocalDate localDate = LocalDate.parse(date, formatter);
			// The date need to be after 1970 to be successfully persisted
			if (localDate.isBefore(LocalDate.of(1970, 01, 02))) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("{error.date.old}").addConstraintViolation();
				return false;
			}
		} catch(DateTimeParseException e) {
			return false;
		}
		return true;
	}	
}