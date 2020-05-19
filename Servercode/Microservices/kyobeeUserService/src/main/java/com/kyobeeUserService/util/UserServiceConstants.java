package com.kyobeeUserService.util;

public class UserServiceConstants {

	// EmailUtil constants
	public static final String EMAIL_FROM = "ria.sahesnani@ordextechnology.com";
	public static final String EMAIL_SERVICE = "AWS_SES";
	public static final String SES_EMAIL_SERVICE = "AWS_SES";
	public static final String EMAIL_SUBJECT = "Forgot Password";
	public static final String ACTIVATION_EMAIL = "Activation Mail";
	public static final String WELCOME_EMAIL = "Welcome Mail";

	public static final Integer SUCCESS_CODE = 1;
	public static final Integer ERROR_CODE = 0;

	public static final String KYOBEE_WEB_HOST = "http://localhost:4200/";
	public static final String KYOBEE_MAIL_ID = "ordextechnology@gmail.com";
	public static final String RESET_PWD_LINK = "reset-password/";

	public static final Integer ENGLISH_LANG_ID = 1;
	public static final Integer SEATING_PREF_ID = 18;
	public static final Integer MARKETING_PREF_ID = 19;

	public static final Byte ACTIVATED_USER = 1;
	public static final String WEB_USER = "Web";

	public static final String FOOTER_MSG = "Copyright KYOBEE. All Rights Reserved - Version 1.3";
	public static final String EXTENSION = ".png";
	public static final String KYOBEE_SERVER_URL = "http://admin.kyobee.com/static/orglogos";

	public static final String AWS_ACCESS_KEY_ID = "AKIAJQJN2NTV6IWMZXMQ";
	public static final String AWS_SECRET_KEY = "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";

	public static final Byte INACTIVE_USER = 0;
	public static final Integer DEFAULT_ROLE = 1;
	public static final Byte ACTIVE_ORG = 1;
	public static final Integer DEFAULT_ORG_TYPE = 1;
	public static final String SMS_ROUTE = "AWS";
	public static final Integer DEFAULT_LANG = 1;

	public static final String SMS_LEVEL_1_NAME = "Check-in";
	public static final String SMS_LEVEL_2_NAME = "Reminder";
	public static final String SMS_LEVEL_3_NAME = "Custom";

	public static final String GOOGLE_API_KEY = "AIzaSyBQxGio9UszquPB1fY23pl60AlAozHa_p0";
	public static final String GEOCODING_API = "https://maps.googleapis.com/maps/api/geocode/json?key="
			+ GOOGLE_API_KEY;
	public static final String AUTOCOMPLETE_API = "https://maps.googleapis.com/maps/api/place/autocomplete/json?types=establishment&radius=1000&strictbounds&key="
			+ GOOGLE_API_KEY;
	public static final String PLACE_DETAILS_API = "https://maps.googleapis.com/maps/api/place/details/json?key="
			+ GOOGLE_API_KEY;
	public static final String GEOCODE_PARAM = "&components=postal_code:";
	public static final String PLACE_PARAM = "&input=";
	public static final String LOCATION_PARAM = "&location=";
	public static final String COUNTRY_PARAM = "&components=country:";
	public static final String PLACE_DETAILS_PARAM = "&place_id=";

	public static final String UTIL_BASE_URL = "http://kyobee-util-service/";
	public static final String EMAIL_PATH = "rest/util/sendEmail";
	public static final String TO_EMAIL = "toEmail";
	public static final String SUBJECT = "subject";
	public static final String BODY = "body";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String ACCEPT = "Accept";
	public static final String CONTENT_TYPE_VALUE = "application/json";
	public static final String API_VERSION = "application/vnd.kyobee.v1+json";
	public static final String VELOCITY_PROPERTY = "classpath.resource.loader.class";
	public static final String NAME = "name";
	public static final String EMAIL = "email";
	public static final String LINK = "link";
	public static final String KYOBEE_EMAIL = "kyobeeEmail";
	public static final String ACTIVATION_CODE = "activationCode";
	public static final String INACTIVE_PLAN = "N";
	public static final Byte INACTIVE = 0;
	public static final String PENDING = "Pending";
	public static final String ADMIN = "admin";

	public static final String PERC = "Percentage";
	public static final String AMOUNT = "Amount";
	public static final Byte ACTIVE = 1;

	// Braintree constants
	public static final String BT_ENVIRONMENT = "sandbox";
	public static final String BT_MERCHANT_ID = "k5dtfbdcdw6brmhv";
	public static final String BT_PUBLIC_KEY = "vb2cv6dtn5ydthmx";
	public static final String BT_PRIVATE_KEY = "0ef72dc04a5f98238039b1796cd669d4";

	public static final String INVOICE_STATUS_BILLED = "Billed";
	public static final String INVOICE_STATUS_PAID = "Paid";
	public static final String CANCELLED = "Cancelled";

	public static final String SUBSCRIBED = "Subscribed";
	public static final String SUCCESS = "Success";
	public static final String FAIL = "Failed";
	public static final String INPROGRESS = "InProgress";
	public static final String ACTIVE_SUBSC = "Y";
	
	public static final String PPL_BIFURCATION = "N";
	public static final Integer CUSTOMER_ADMIN_ROLE=4;
	
	public static final String STATUS_Y ="Y";
	
	public static final Integer MAX_PARTY = 100;
	public static final Integer NOTIFY_FIRST = 3;

}
