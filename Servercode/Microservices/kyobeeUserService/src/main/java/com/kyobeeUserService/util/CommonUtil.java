package com.kyobeeUserService.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CommonUtil {

	public static String encryptPassword(String password) {

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			byte[] messageDigest = md.digest(password.getBytes());

			BigInteger no = new BigInteger(1, messageDigest);

			String hashtext = no.toString(16);

			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			LoggerUtil.logError(e);
			return null;
		}
	}

	public static Long generateRandomToken() {
		return (long) (Math.random() * 1000000);
		// 0000
	}

	public static Date getDateByHour(Integer hours) {
		Date today = new Date();
		long hour = 3600 * 1000;
		Date nextDay = new Date(today.getTime() + hours * hour);
		return nextDay;

	}

	public static String getSaltString() {

		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();

		while (salt.length() < 6) {

			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));

		}

		return salt.toString();

	}
	public static String getDateWithFormat(String format) throws ParseException {

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
}
