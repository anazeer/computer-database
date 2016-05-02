package com.excilys.cdb.validation;

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
	
}