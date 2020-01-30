package com.kyobeeWaitlistService.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {

	private static Logger log = LogManager.getLogger(LoggerUtil.class);
	
	public static void logInfo(String string) {
		log.info(string);
	}

	public static void logDebug(String message) {
		log.debug(message);
	}
	
	public static void logDebug(String message, Throwable t) {
		log.debug(message, t);
	}
	
	public static void logError(String message) {
		log.error(message);
	}

	public static void logError(String message, Throwable t) {
		log.error(message, t);
	}


	public static void logError(Object printStackTrace) {
	
		log.error(printStackTrace);
		
	}
}
