package com.kyobeeUserService.util.Exception;

public class InvalidAuthCodeException extends Exception{

private static final long serialVersionUID = 1L;
	
	public InvalidAuthCodeException(String e) {
		
		super(e);
		
	}
}
