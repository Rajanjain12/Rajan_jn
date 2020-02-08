package com.kyobeeWaitlistService.util;

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

}
