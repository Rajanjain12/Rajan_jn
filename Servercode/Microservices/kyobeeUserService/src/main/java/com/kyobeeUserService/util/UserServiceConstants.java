package com.kyobeeUserService.util;

public class UserServiceConstants {

	// EmailUtil constants
	public static final String MAILHOST = "mail.smtp.host";
	public static final String MAILSERVERPORT = "mail.smtp.port";
	public static final String SMTPAUTH = "mail.smtp.auth";
	public static final String ENABLESTARTTLS = "mail.smtp.starttls.enable";
	public static final String EMAIL_FROM = "ria.sahesnani@ordextechnology.com";
	public static final String EMAIL_SERVICE = "AWS_SES";
	public static final String SES_EMAIL_SERVICE = "AWS_SES";
	public static final String EMAIL_SUBJECT = "Forgot Password";
	public static final String ACTIVATION_EMAIL = "Activation Mail";
	public static final String WELCOME_EMAIL = "Welcome Mail";
	
	
	public static final Integer SUSSESS_CODE=1;
	public static final Integer ERROR_CODE=0;
	
	public static final String KYOBEEWEBHOST = "http://localhost:4200/";
	public static final String KYOBEEMAILID = "ordextechnology@gmail.com";
    public static final String RESETPWDLINK="reset-password/";
	
	public static final Integer ENGLISHLANGID=1;
	public static final Integer SEATINGPREFID=18;
	public static final Integer MARKETINGPREFID=19;
	
	public static final Byte ACTIVATEDUSER=1;
	public static final String WEBUSER="Web";
	
	public static final String FOOTERMSG="Copyright KYOBEE. All Rights Reserved - Version 1.3";
	public static final String EXTENSION=".png";
	public static final String KYOBEESERVERURL="http://admin.kyobee.com/static/orglogos";
	
	public static final String AWS_ACCESS_KEY_ID="AKIAJQJN2NTV6IWMZXMQ";
	public static final String AWS_SECRET_KEY="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";
	
	public static final Byte INACTIVEUSER=0;
	public static final Integer DEFAULT_ROLE=1;
	public static final Byte ACTIVEORG=1;
	public static final Integer DEFAULT_ORG_TYPE=1;
	public static final String SMS_ROUTE="AWS";
	public static final Integer DEFAULT_LANG=1;
	
	
}
