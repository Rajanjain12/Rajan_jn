package com.kyobeeWaitlistService.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

	public static String buildURL(String clientBase, String uuid) {

		String url = "";

		switch (clientBase) {
		case WaitListServiceConstants.ADMIN:
			url = WaitListServiceConstants.ADMIN_URL + "/s/" + uuid;
			break;
		case WaitListServiceConstants.ADVANTECH:
			url = WaitListServiceConstants.ADVANTECH_URL + "/s/" + uuid;
			break;
		case WaitListServiceConstants.SWEETHONEYDESSERT:
			url = WaitListServiceConstants.SWEETHONEYDESSERT_URL + "/s/" + uuid;
			break;
		case WaitListServiceConstants.RBSUSHI:
			url = WaitListServiceConstants.RBSUSHI_URL + "/s/" + uuid;
			break;
		case WaitListServiceConstants.MASTERKIM:
			url = WaitListServiceConstants.MASTERKIM_URL + "/s/" + uuid;
			break;
		default:
			url = WaitListServiceConstants.URL_INTITIAL + clientBase + "." + WaitListServiceConstants.URL_SUFFIX + uuid;
		}

		return url;
	}

	public static Date getCurrentDateWithTime() throws ParseException{

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date = dateFormat.parse(dateFormat.format(date));
		return date;
	}

}
