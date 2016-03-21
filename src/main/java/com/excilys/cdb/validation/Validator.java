package com.excilys.cdb.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.excilys.cdb.exception.DateException;
import com.excilys.cdb.exception.IdException;
import com.excilys.cdb.exception.NameException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.ServiceFactory;

/**
 * User inputs validation
 * @author excilys
 *
 */
public class Validator {
	
	private static CompanyService companyService = ServiceFactory.getCompanyService();
	private static ComputerService computerService = ServiceFactory.getComputerService();
	
	public static final String NULL_NAME = "name is required";
	public static final String EMPTY_NAME = "name should not be empty";
	public static final String ILLEGAL_NAME = "illegal character in name (only alphanumeric and .+/)('- are allowed";
	public static final String NULL_DATE = "date is required";
	public static final String INCORRECT_DATE = "date format is incorrect";
	public static final String ILLEGAL_DATE = "date is illegal";
	public static final String COMP_ERROR = "computer creation failed (date might be illegal, for example too old such as 1111-11-11)";
	public static final String COMP_SUCCESS = "Operation successful!";
	public static final String ILLEGAL_ID = "the id should be an positive integer";
	
	/**
	 * Validate a date. It should be like yyyy-mm-dd with valid numbers
	 * @param date the date input
	 * @throws DateException
	 */
	public static void dateValidator(String date) throws DateException {
		if(date == null) {
			return;
		}
		String parse = "yyyy-mm-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(parse);
		sdf.setLenient(false);
		try {
			sdf.parse(date);
		}
		catch (ParseException e) {
			throw new DateException(INCORRECT_DATE);
		}
		try {
			LocalDate.parse(date);
		}
		catch(DateTimeParseException e) {
			throw new DateException(ILLEGAL_DATE);
		}
	}
	
	/**
	 * Validate a name. It's composed of alphanumeric and +/)('-] with at least one letter at the beginning
	 * @param name the name input
	 * @throws NameException
	 */
	public static void nameValidator(String name) throws NameException {
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
	 * Validate a company ID. It should be a valid integer in the company range
	 * @param companyId the company id input
	 * @throws IdException
	 */
	public static void companyIdValidator(String companyId) throws IdException {
		if(companyId == null) {
			return;
		}
		Long id;
		boolean good = true;
		try {
			id = Long.parseLong(companyId);
			if(id < 0 || id > companyService.count()) {
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
	 * Validate a computer ID. It should be a valid integer in the computer range
	 * @param computerId the computer id input
	 * @throws IdException
	 */
	public static void computerIdValidator(String computerId) throws IdException {
		if(computerId == null) {
			return;
		}
		Long id;
		boolean good = true;
		try {
			id = Long.parseLong(computerId);
			if(id < 0 || id > computerService.count()) {
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
}