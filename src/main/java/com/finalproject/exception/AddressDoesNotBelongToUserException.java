package com.finalproject.exception;



public class AddressDoesNotBelongToUserException extends IllegalArgumentException {
	
	public AddressDoesNotBelongToUserException(String msg) {
		super(msg);
	}

}