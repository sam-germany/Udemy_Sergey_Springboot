package com.sunny.admin.exceptions;

public class UserServiceException extends RuntimeException{

	private static final long serialVersionUID = 7959516810631568358L;

	
	public UserServiceException(String message) {
		super(message);
	}
	
}
