package com.bootcampproject.bootcamp_project.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static boolean isValidatedPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,15}";
        Pattern p = Pattern.compile(regex); //Pattern 1 class hoti h jo regex ko compile krti h
        Matcher m = p.matcher(password);
        return m.matches();
    }
    public static boolean isValidatedGST(String gst) {
        String regex = "^[0-9]{2}[A-Z]{5}[0-9]{4}"
                + "[A-Z]{1}[1-9A-Z]{1}"
                + "Z[0-9A-Z]{1}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(gst);
        return m.matches();
		/*
		^ represents the starting of the string.
[0-9]{2} represents the first two characters should be a number.
[A-Z]{5} represents the next five characters should be any upper case alphabets.
[0-9]{4} represents the next four characters should be any number.
[A-Z]{1} represents the next character should be any upper case alphabet.
[1-9A-Z]{1} represents the 13th character should be a number from 1-9 or an alphabet.
				Z represents the 14th character should be Z.
[0-9A-Z]{1} represents the 15th character should be an alphabet or a number.
				$ represents the ending of the string*/
    }
}
