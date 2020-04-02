package com.kyobeeWaitlistService.util;

public class WaitListServiceConstants {

	public static final Integer SUCCESS_CODE = 1;
	public static final Integer ERROR_CODE = 0;

	public static final Byte ACTIVATED_USER = 1;
	public static final String WEB_USER = "Web";

	public static final String PUSHER_CHANNEL_ENV = "RSNT_GUEST_DEV";
	public static final String GLOBAL_CHANNEL_ENV = "RSNT_GLOBAL_DEV";
	public static final String PUBNUB_SUBSCRIBE_KEY = "sub-c-a408d714-2b8c-11ea-894a-b6462cb07a90";
	public static final String PUBNUB_PUBLISH_KEY = "pub-c-cf81e7f1-47b0-4559-a127-720cd3085a92";
	public static final String PUBNUB_SECRET_KEY = "sec-c-YjU2ZDZhYWEtZjFiMC00N2U5LTk3ZGMtNTliMmM4ZTY4NTcx";

	public static final Integer MARK_AS_LANGUAGE_UPDATED = 1;
	public static final Integer MARK_AS_LANGUAGE_RESET = 0;

	public static final String ADMIN_URL = "https://tinyurl.com/u8f94j9";
	public static final String ADVANTECH_URL = "https://tinyurl.com/y7nl9ugt";
	public static final String SWEETHONEYDESSERT_URL = "https://tinyurl.com/y95p4eug";
	public static final String RBSUSHI_URL = "https://tinyurl.com/ybnzpy9w";
	public static final String MASTERKIM_URL = "https://tinyurl.com/y9augjfg";
	
	public static final String ADMIN = "admin";
	public static final String ADVANTECH = "advantech";
	public static final String SWEETHONEYDESSERT = "sweethoneydessert";
	public static final String RBSUSHI = "rbsushi";
	public static final String MASTERKIM = "masterkim";

	public static final String URL_INTITIAL = "http://";
	public static final String URL_SUFFIX = "ordextechnology.com:8080/kyobee/public/index.html#/s?tid=";
	
	public static final String DELETE_STATUS="DELETE";
	public static final String INCOMPLETE_STATUS="INCOMPLETE";
	public static final String NOTPRESENT_STATUS="NOTPRESENT";
	public static final String SEATED_STATUS="SEATED";
	public static final String UPDATE_STATUS="UPDATE";
	public static final String ADD_STATUS="ADD";
	public static final String FROM_ADMIN="ADMIN";
	
	public static final Integer SEATING_PREF_ID=18;
	public static final Integer MARKETING_PREF_ID=19;

    public static final String SCREEN_NAME="seatingOrMarketingPref";
	public static final String 	ENG_ISO_CODE="en";
	
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
	
	public static final String PROCESS = "P";
	public static final Integer PHONE_NO_PREFIX = +91;
	public static final Integer TEMP_LEVEL_FIRST = 1;
	public static final Integer TEMP_LEVEL_SECOND = 2;
	public static final String SEND_SMS_API = "http://localhost:8082/rest/waitlist/sendSMS";
	public static final String SMS_SIGNATURE="SweetHoneyDessert";
	
	public static final Byte ACTIVE = 1;
	
	public static final String SMS_LEVEL_1_NAME="Check-in";
	public static final String SMS_LEVEL_2_NAME="Reminder";
	public static final String SMS_LEVEL_3_NAME="Custom";
	
	public static final String ADD_LANG_PUSHER ="ADD_LANGUAGE_PUSHER";
	public static final String DELETE_LANG_PUSHER ="DELETE_LANGUAGE_PUSHER";
	public static final String SETTING_PUSHER ="ORG_SETTING_PUSHER";
	public static final String RESET_ORGANIZATION_PUSHER ="RESET_ORGANIZATION_PUSHER";
	public static final String REFRESH_LANGUAGE_PUSHER ="REFRESH_LANGUAGE_PUSHER";
	public static final String NOTIFY_USER ="NOTIFY_USER";
	
	

}
