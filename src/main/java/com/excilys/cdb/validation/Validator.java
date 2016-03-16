package com.excilys.cdb.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Validator {

	public static boolean dateValidator(String date) {
		String parse = "dd/mm/yyyy";
		if(date == null) {
			return true;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(parse);
		sdf.setLenient(false);
		try {
			sdf.parse(date);
		}
		catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	public static boolean nameValidator(String name) {
		if(name == null) {
			return false;
		}
		name = name.trim();
		if(name.isEmpty())
			return false;
		return true;
	}

}