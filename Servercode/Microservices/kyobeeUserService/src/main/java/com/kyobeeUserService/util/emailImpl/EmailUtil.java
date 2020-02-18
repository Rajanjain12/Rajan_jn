package com.kyobeeUserService.util.emailImpl;

import org.springframework.stereotype.Component;

import com.kyobeeUserService.util.UserServiceConstants;
import com.kyobeeUserService.util.email.Email;

@Component
public class EmailUtil {

	static Email email;

	static {
		switch (UserServiceConstants.EMAIL_SERVICE) {
		case UserServiceConstants.SES_EMAIL_SERVICE:
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
