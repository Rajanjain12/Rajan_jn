package com.kyobeeUtilService.service;

public interface UtilService {
	
	public void sendSMS(String contactNo, String message);
	
	public void sendEmail(String toEmail, String subject, String body);

}
