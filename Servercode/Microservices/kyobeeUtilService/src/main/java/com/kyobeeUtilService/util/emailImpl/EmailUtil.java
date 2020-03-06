package com.kyobeeUtilService.util.emailImpl;

import org.springframework.stereotype.Component;

import com.kyobeeUtilService.util.UtilServiceConstants;
import com.kyobeeUtilService.util.email.Email;

@Component
public class EmailUtil {

	static Email email;

	static {
		switch (UtilServiceConstants.EMAIL_SERVICE) {
		case UtilServiceConstants.SES_EMAIL_SERVICE:
			email = new AWSUtil();
			break;

		default:
			email = new AWSUtil();
			break;
		}
	}

	public static void sendMail(String toEmail, String subject, String body) {
		email.sendEmail(toEmail, subject, body);
	}

}
