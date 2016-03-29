package com.excilys.cdb.validation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.excilys.cdb.exception.DateException;
import com.excilys.cdb.exception.IdException;
import com.excilys.cdb.exception.NameException;
import com.excilys.cdb.service.dto.ComputerDTO;

/**
 * User inputs validation
 */
public class Validator {
	
	// Error messages
	public static final String NULL_NAME = "name is required";
	public static final String EMPTY_NAME = "name should not be empty";
	public static final String ILLEGAL_NAME = "illegal character in name (only alphanumeric and .+/)('- are allowed";
	public static final String NULL_DATE = "date is required";
	public static final String INCORRECT_DATE = "date format is incorrect";
	public static final String OLD_DATE = "date is too old";
	public static final String INTRO_AFTER_DISC = "discontinued date should be after introduced date";
	public static final String CREATE_ERROR = "Computer creation failed";
	public static final String EDIT_ERROR = "Computer edition failed";
	public static final String COMP_SUCCESS = "Operation successful!";
	public static final String ILLEGAL_ID = "the id should be an positive integer";
	
    // ID for the addComputer JSP for error messages
    private static final String nameError= "vcomputerName";
    private static final String introError = "vintroduced";
    private static final String discontinuedError = "vdiscontinued";
    private static final String globalError = "vfailure";
	
    /**
	 * Validate a date. It should be like yyyy-mm-dd with valid numbers
     * @param date the date string
     * @return the LocalDate parsed
     * @throws DateException
     */
	public static LocalDate validateDate(String date) throws DateException {
		LocalDate d = null;
		if(date == null) {
			return d;
		}
		try {
			d = LocalDate.parse(date);
			if (d.isBefore(LocalDate.of(1970, 01, 02))) {
				throw new DateException(OLD_DATE);
			}
		} catch (DateTimeParseException e) {
			throw new DateException(INCORRECT_DATE);
		}
		return d;
	}
	
	/**
	 * 
	 * Validate a name. It's composed of alphanumeric and +/)('-] with at least one letter at the beginning
	 * @param name the name input
	 * @throws NameException
	 */
	public static void validateName(String name) throws NameException {
		if(name == null) {
			throw new NameException(NULL_NAME);
		}
		name = name.trim();
		if(name.isEmpty())
			throw new NameException(EMPTY_NAME);
		Pattern pattern = Pattern.compile("^[\\p{L}][\\p{L} 0123456789.+/)('-]*$");
		Matcher matcher = pattern.matcher(name);
		if(!matcher.find()) {
			throw new NameException(ILLEGAL_NAME);
		}
	}
	
	/**
	 * Validate an ID. It should be a valid integer
	 * @param id the id input
	 * @throws IdException
	 */
	public static void validateId(String id) throws IdException {
		if(id == null) {
			throw new IdException(ILLEGAL_ID);
		}
		Long id_long;
		boolean good = true;
		try {
			id_long = Long.parseLong(id);
			if(id_long < 0) {
				good = false;
			}
		}
		catch(NumberFormatException e) {
			good = false;
		}
		if(!good) {
			throw new IdException(ILLEGAL_ID);
		}
	}
	
	/**
	 * Validate a computer DTO
	 * @param dto the computer DTO
	 * @return the map containing all the errors associated to the JSP ID
	 */
	public static Map<String, String> validateComputer(ComputerDTO dto) {
		
		// Map for error messages
		Map<String, String> errors = new HashMap<>();
		LocalDate introduced = null;
		LocalDate discontinued = null;
		
		// Check the validity of the DTO and put error messages in the map
		try {
			Validator.validateName(dto.getName());
		} catch (NameException e) {
			errors.put(nameError, e.getMessage());
		}
		try {
			introduced = Validator.validateDate(dto.getIntroduced());
		} catch (DateException e) {
			errors.put(introError, e.getMessage());
		} 
		try {
			discontinued = Validator.validateDate(dto.getDiscontinued());
			if (introduced != null && discontinued != null
	                && introduced.isAfter(discontinued)) {
				errors.put(discontinuedError, INTRO_AFTER_DISC);
			}
			
		} catch (DateException e) {
			errors.put(discontinuedError, e.getMessage());
		}
		try {
			Validator.validateId(dto.getCompanyId().toString());
		} catch (IdException e) {
			errors.put(globalError, e.getMessage());
		}
		
		// Return the errors map. If no errors were found, the map will be empty
		return errors;
	}
	
}