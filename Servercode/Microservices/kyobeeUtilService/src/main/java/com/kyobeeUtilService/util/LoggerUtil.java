package com.kyobeeUtilService.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kyobeeUtilService.util.LoggerUtil;

public class LoggerUtil {

	private static Logger log = LogManager.getLogger(LoggerUtil.class);
	
	public static void logInfo(String message) {
		log.info(message);
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
