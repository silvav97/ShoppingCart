package com.finalproject.exception;

import org.springframework.http.HttpStatus;


public class CartItemDoesNotBelongToUserException extends IllegalArgumentException {
	
	public CartItemDoesNotBelongToUserException(String msg) {
		super(msg);
	}

}