package com.kyobeeUserService.util.Exception;

public class TransactionFailureException extends Exception{

private static final long serialVersionUID = 1L;
	
	public TransactionFailureException(String e) {
		
		super(e);
		
	}
}
