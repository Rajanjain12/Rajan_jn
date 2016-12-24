package com.kyobee.util.common;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

/**
 * This is the logging utility used for logging the messages. We have provided
 * this abstraction, so that we can change the logging without affecting the
 * entire code just by changing this file.
 * 
 * @author rohit
 *
 */
@Component
public class LoggerUtil {

	private static Logger log = Logger.getLogger(LoggerUtil.class);

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
}
