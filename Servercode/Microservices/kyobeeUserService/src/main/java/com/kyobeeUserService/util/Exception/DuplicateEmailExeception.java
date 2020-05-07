package com.kyobeeUserService.util.Exception;

public class DuplicateEmailExeception extends Exception{

private static final long serialVersionUID = 1L;
	
	public DuplicateEmailExeception(String e) {
		
		super(e);
		
	}
}
