package com.kyobee.util.common;

public class Constants {
	
	
	public static final String ERROR = "ERROR";
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILURE = "FAILURE";
	public static final String FREETEXT = "FREETEXT";
	
	
	public static final String USER_OBJ = "USER_SESSION_OBJ";
	public static final CharSequence LOGOUT_URL = "/logout";
	
	public static final String PUSHER_CHANNEL_ENV="pusherChannelEnv";
	public static final String QRCODE_VALUE="qrcodevalue";

	public static final String CONST_ORGID = "orgId";
	public static final String CONST_ORGNAME = "orgName";
	public static final String CONST_ORGSUBSCRIBEDPLANNAME = "orgSusPlanName";
	public static final String CONST_ORGSUBSCRIPTIONPLANID = "orgSubPlanId";
	public static final String CONST_ORGADSBALANCE = "orgAdsBalance";
	public static final String CONST_USERROLEID = "userRoleId";
	
	public static final int CONST_ORG_TYPE_ID=1;
	public static final int CONST_ORG_PLAN_SUBSCRIPTION_ID=1;
	
	public static final int CONST_LOOKUP_PLANTYPE_FREE = 7;
	public static final int CONST_LOOKUP_PLANTYPE_PAID = 8;
	
	public static final int CONT_LOOKUP_ROLE_PRDADMIN = 5;
	public static final int CONT_LOOKUP_ROLE_RSNTADMIN = 16;
	public static final int CONT_LOOKUP_ROLE_RSNTSTAFF = 17;
	public static final int CONT_LOOKUP_ROLE_RSNTMANAGER = 44;
	public static final int CONT_LOOKUPTYPE_ROLE = 2;
	
	public static final String LOOKUP_ROLE_RSNTSTAFF = "Staff";
	public static final String LOOKUP_ROLE_RSNTMANAGER = "Manager";
	
	public static final int CONT_LOOKUPTYPE_FEATURE_ALERT = 1;
	public static final int CONT_LOOKUPTYPE_FEATURE_REPORT= 12;
	public static final int CONT_LOOKUPTYPE_FEATURE_FEEDBACK= 11;
	public static final int CONT_LOOKUPTYPE_FEATURE_PROMOTIONALOFFERS= 13;
	public static final int CONT_LOOKUPTYPE_FEATURE_SPECIALADS= 14;
	public static final int CONT_LOOKUPTYPE_FEATURE_OPTIONCREATE= 15;
	
	
	public static final int CONT_LOOKUP_PLAN_UNIT_MONTH = 9;
	public static final int CONT_LOOKUP_PLAN_UNIT_YEAR = 10;
	public static final int CONT_LOOKUP_PLAN_UNIT_DAY= 32;
	
	public static final int CONT_LOOKUPTYPE_CURRENCY= 6;
	public static final int CONT_LOOKUPTYPE_PLANTYPE= 4;
	public static final int  CONT_LOOKUPTYPE_PLANALLALERTTYPE= 1;
	public static final int CONT_LOOKUPTYPE_CARDTYPE= 7;
	public static final int CONT_LOOKUP_CARDTYPE_VISA= 13;
	
	public static final int CONT_LOOKUPTYPE_CUISINETYPE= 9;
	public static final int CONT_LOOKUPTYPE_DISHTYPE= 10;
	public static final int CONT_LOOKUPTYPE_RSNTTYPE= 8;
	public static final int CONT_LOOKUPTYPE_SEATTYPE= 18;
	
	public static final int CONT_LOOKUP_FEATURE_ADDITIONALOFFERS = 31;
	public static final int CONT_LOOKUP_FEEDBACKTYPE_SIMPLE= 27;
	public static final int CONT_LOOKUP_FEEDBACKTYPE_ADVANCED= 28;
	///public static final int CONT_LOOKUPTYPE_REPORTTYPE= 12;
	
	public static final int CONT_LOOKUP_CURRENCY_USD= 11;
	
	public static final String DEVICE_MAKE_APPLE = "AP";
	public static final String DEVICE_MAKE_ANDROID = "AN";
	
	public static final int AD_CREDIT_TYPE_FREE = 1;
	public static final int AD_CREDIT_TYPE_PAID = 2;
	
	public static final int CONT_LOOKUP_IMAGE_TYPE_BANNER= 33;
	public static final int CONT_LOOKUP_IMAGE_TYPE_ADDON= 34;
	
	public static final String CREATED_BY_SYSTEM = "sys";
	
	public static final String OPEN_BRACKET = "{call ";
	public static final String CLOSE_BRACKET = " }";
	
	public static final int CONT_LOOKUPTYPE_FEEDBACKQUESTIONTYPE= 17;
	
	public static final int CONT_LOOKUP_FEEDBACKANSWERTYPEOPTION_COMMENT= 35;
	public static final int CONT_LOOKUP_FEEDBACKANSWERTYPEOPTION_SINGLE_OPTION= 36;
	public static final int CONT_LOOKUP_FEEDBACKANSWERTYPEOPTION_MULTI_OPTION= 37;
	
	public static final int CONT_ERR_ADDRESS_PARIAL_MATCH =1 ;
	public static final int CONT_ERR_ADDRESS_APPROX_MATCH =2;
	public static final int CONT_ERR_ADDRESS_NO_MATCH =3 ;
	public static final String PUSHER_PUBLICK_KEY= "PUSHER_PUBLICK_KEY";
	public static final String PUSHER_PUBLICK_KEY_VAL= "rsnt.pusher.publickey";
	public static final String PUSHER_PRIVATE_KEY= "PUSHER_PRIVATE_KEY";
	public static final String PUSHER_PRIVATE_KEY_VAL= "rsnt.pusher.privatekey";
	public static final String RSNT_GUEST= "guest";
	public static final String RSNT_ERROR= "error";
	
	public static final String RSNT_GUEST_SEATED= "SEATED";
	public static final String RSNT_GUEST_DELETED= "DELETED";
	public static final String RSNT_GUEST_SUCCESS= "SUCCESS";
	public static final String RSNT_GUEST_FAIL= "FAIL";
	
	public static final String TOTAL_WAIT_TIME = "TOTAL_WAIT_TIME";
	public static final String ORG_MAX_PARTY = "ORG_MAX_PARTY";
	
	public static final String RSNT_ORG_WAIT_TIME ="ORG_WAIT_TIME";
	public static final String RSNT_ORG_TOTAL_WAIT_TIME ="ORG_TOTAL_WAIT_TIME";
	public static final String RSNT_ORG_GUEST_COUNT ="ORG_GUEST_COUNT";
	public static final String RSNT_ORG_NOTIFY_COUNT ="ORG_NOTIFY_COUNT";
	public static final String RSNT_GUEST_AHEAD_COUNT ="GUEST_AHEAD_COUNT";
	public static final String RSNT_GUEST_RANK = "GUEST_RANK_MAX";
	public static final String RSNT_GUEST_RANK_MIN = "GUEST_RANK_MIN";
	public static final String RSNT_EMAIL = "EMAIL";
	public static final String RSNT_SMS = "SMS";
	public static final String RSNT_ORG_ID = "orgId";
	public static final String RSNT_GUEST_UUID = "UUID";
	public static final String REALTIME_APPLICATION_KEY= "REALTIME_APPLICATION_KEY";
	public static final String REALTIME_APPLICATION_KEY_VAL= "rsnt.realtime.appkey";
	public static final String REALTIME_PRIVATE_KEY= "REALTIME_PRIVATE_KEY";
	public static final String REALTIME_PRIVATE_KEY_VAL= "rsnt.realtime.privatekey";
	
	public static final String RSNT_NOW_SERVING_GUEST_ID = "NOW_SERVING_GUEST_ID";
	public static final String RSNT_NEXT_TO_NOTIFY_GUEST_ID =  "NEXT_TO_NOTIFY_GUEST_ID";
	public static final String CONT_LOOKUPTYPE_GUEST_PREF = "18";
	public static final String CONT_LOOKUPTYPE_GUEST_MARKETING_PREF = "19";
	public static final int WAITLIST_UPDATE_GUEST = 1;
	public static final int WAITLIST_UPDATE_CALLOUT = 2;
	public static final int WAITLIST_UPDATE_MARK_AS_SEATED = 3;
	public static final int WAITLIST_UPDATE_INCOMPLETE = 4;
	public static final int WAITLIST_UPDATE_DELETE = 5;
	
	public static final String NOTIF_MARK_AS_SEATED = "MARK_AS_SEATED";
	public static final String NOTIF_THRESHOLD_ENTERED = "THRESHOLD_ENTERED";
	public static final String NOTIF_CHECKIN = "NORMAL";
    
	public static final int REALTIME_API_GET_SUCCESS_CODE = 200;
	public static final int REALTIME_API_POST_SUCCESS_CODE = 201;
	
	public static final String FOOTER_MSG = "FOOTER_MSG";
	
	public static final String AWS_ACCESS_KEY_ID="AKIAJQJN2NTV6IWMZXMQ";
	public static final String AWS_SECRET_KEY="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";
	
	public static final String SNS_DEFAULT_SENDER_ID = "KYOBEE";
	public static final String SNS_MONTHLY_SPEND_LIMIT = "1000";
	public static final String SNS_DELIVERY_STATUS_IAM_ROLE = "arn:aws:iam::437586897314:role/snscloudwatchrole";
	public static final String SNS_DELIVERY_STATUS_SUCCESS_SAMPLING_RATE = "100";
	public static final String SNS_DEFAULT_SMS_TYPE = "Transactional";
	public static final String SNS_USAGE_REPORT_S3_BUCKET = "smsusagekyobee";
	
	
	
}
