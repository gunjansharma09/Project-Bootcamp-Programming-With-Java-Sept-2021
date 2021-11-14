package com.bootcampproject.bootcamp_project.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

	public static boolean isValidatedPassword(String password) {
		String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}";
		Pattern p = Pattern.compile(regex); //Pattern 1 class hoti h jo regex ko compile krti h
		Matcher m = p.matcher(password);
		return m.matches();
	}
}
