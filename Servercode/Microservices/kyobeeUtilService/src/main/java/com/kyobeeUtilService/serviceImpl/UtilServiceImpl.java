package com.kyobeeUtilService.serviceImpl;

import org.springframework.stereotype.Service;

import com.kyobeeUtilService.service.UtilService;
import com.kyobeeUtilService.util.emailImpl.EmailUtil;
import com.kyobeeUtilService.util.message.MessageNotification;
import com.kyobeeUtilService.util.messageImpl.SMSUtil;
@Service
public class UtilServiceImpl implements UtilService{

	@Override
	public void sendSMS(String contactNo, String message) {
		
		SMSUtil.sendSMS(contactNo, message);
	}

	@Override
	public void sendEmail(String toEmail, String subject, String body) {
		
		EmailUtil.sendMail(toEmail, subject, body);
		
	}

}
