package com.kyobeeWaitlistService.util.smsImpl;

import com.kyobeeWaitlistService.util.WaitListServiceConstants;
import com.kyobeeWaitlistService.util.sms.SMS;

public class SMSUtil {

	static SMS sms;

	static {
		switch (WaitListServiceConstants.SMS_SERVICE) {
		case WaitListServiceConstants.SNS_SMS_SERVICE:
			sms = new AWSUtil();
			break;

		default:
			sms = new AWSUtil();
			break;
		}
	}

	public static void sendSMS(Integer contactNo, String message) {
		sms.sendMessage(contactNo, message);
	}

}
