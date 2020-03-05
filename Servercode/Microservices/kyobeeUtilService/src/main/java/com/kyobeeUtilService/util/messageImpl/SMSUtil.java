package com.kyobeeUtilService.util.messageImpl;

import com.kyobeeUtilService.util.UtilServiceConstants;
import com.kyobeeUtilService.util.message.MessageNotification;

public class SMSUtil {
	
	static MessageNotification messageNotification;

	static {
		switch (UtilServiceConstants.SMS_SERVICE) {
		case UtilServiceConstants.SNS_SMS_SERVICE:
			messageNotification = new AWSUtil();
			break;

		default:
			messageNotification = new AWSUtil();
			break;
		}
	}

	public static void sendSMS(String contactNo, String message) {
		messageNotification.sendMessage(contactNo, message);
	}

}
