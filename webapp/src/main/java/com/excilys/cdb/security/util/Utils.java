package com.excilys.cdb.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This class is use to encrypt password
 */
public class Utils {
 
    public static void main(String args[]) throws Exception {
        String cryptedPassword = new BCryptPasswordEncoder().encode("titi");
        System.out.println(cryptedPassword);
    }
}