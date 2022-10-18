package com.finalproject.exception;


public class UserDoesNotHaveAShoppingCartException extends IllegalArgumentException {
	
	public UserDoesNotHaveAShoppingCartException(String msg) {
		super(msg);
	}

}
