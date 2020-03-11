package com.kyobeeUtilService.util;

public class UtilServiceConstants {

	public static final Integer SUCCESS_CODE = 1;
	public static final Integer ERROR_CODE = 0;
	
	public static final Integer PHONE_NO_PREFIX = +91;
	
	public static final String SNS_DEFAULT_SENDER_ID = "KYOBEE";
	public static final String SNS_MONTHLY_SPEND_LIMIT = "1000";
	public static final String SNS_DELIVERY_STATUS_IAM_ROLE = "arn:aws:iam::437586897314:role/snscloudwatchrole";
	public static final String SNS_DELIVERY_STATUS_SUCCESS_SAMPLING_RATE = "100";
	public static final String SNS_DEFAULT_SMS_TYPE = "Transactional";
	public static final String SNS_USAGE_REPORT_S3_BUCKET = "smsusagekyobee";
	
	public static final String AWS_ACCESS_KEY_ID="AKIAJQJN2NTV6IWMZXMQ";
	public static final String AWS_SECRET_KEY="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";
	public static final String AWS_REGION="us-west-2";
	
	public static final String SMS_SERVICE = "AWS SNS";
	public static final String SNS_SMS_SERVICE = "AWS SNS";
	
	// EmailUtil constants
	public static final String EMAIL_FROM = "ria.sahesnani@ordextechnology.com";
	public static final String EMAIL_SERVICE = "AWS_SES";
	public static final String SES_EMAIL_SERVICE = "AWS_SES";
	public static final String EMAIL_SUBJECT = "Forgot Password";
	public static final String ACTIVATION_EMAIL = "Activation Mail";
	public static final String WELCOME_EMAIL = "Welcome Mail";
	
}
