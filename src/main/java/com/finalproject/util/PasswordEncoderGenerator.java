package com.finalproject.util;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.finalproject.FinalProjectApplication;

public class PasswordEncoderGenerator {
	
	public static void main(String[] args) {
		
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode("password"));
	}

}
