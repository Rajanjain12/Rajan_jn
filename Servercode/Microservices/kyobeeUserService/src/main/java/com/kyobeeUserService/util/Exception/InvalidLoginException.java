package com.kyobeeUserService.util.Exception;

public class InvalidLoginException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public InvalidLoginException(String e) {
		
		super(e);
		
	}
}
