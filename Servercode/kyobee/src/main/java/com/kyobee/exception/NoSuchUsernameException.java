/**
 * 
 */
package com.kyobee.exception;

/**
 * @author Vruddhi
 *
 */
public class NoSuchUsernameException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoSuchUsernameException(String message) {
		super(message);
	}

}