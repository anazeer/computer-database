package com.excilys.cdb.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.excilys.cdb.exception.DateException;
import com.excilys.cdb.exception.NameException;

public class Validator {
	
	public static final String NULL_NAME = "name is required";
	public static final String EMPTY_NAME = "name should not be empty";
	public static final String ILLEGAL_NAME = "illegal character in name (only alphanumeric and .+/)('- are allowed";
	public static final String NULL_DATE = "date is required";
	public static final String INCORRECT_DATE = "date format is incorrect";
	public static final String ILLEGAL_DATE = "date is illegal";
	public static final String COMP_ERROR = "computer creation failed (date might be illegal, for example too old such as 1111-11-11)";
	public static final String COMP_SUCCESS = "Computer successfully added !";
	
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

}