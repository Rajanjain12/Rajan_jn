package com.kyobee.util.common;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.kyobee.dto.AdUsageHistoryReportDTO;
import com.kyobee.dto.AdUsageWrapperDTO;
import com.kyobee.dto.common.Response;
import com.kyobee.exception.RsntException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javapns.notification.PushNotificationPayload;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * This is a Common Utility Class for reusable methods.
 * 
 * @author hpawar
 */

public class CommonUtil {

	//@Autowired
	//private SessionFactory sessionFactory;

	//private Logger log = Logger.getLogger(CommonUtil.class);

	/*public static Long getOrgPlanType() throws RsntException {
		try {
			return ((BigInteger) entityManager.createNativeQuery(NativeQueryConstants.GET_ORGANIZATION_PLAN_TYPE)
					.setParameter(1, Contexts.getSessionContext().get(Constants.CONST_ORGID).toString())
					.getSingleResult()).longValue();
		} catch (Exception e) {
			log.error("LayoutServiceImpl.getOrgPlanType()", e);
			throw new RsntException(e);
		}
	}*/

	private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json";

	private static CommonUtil cmnUtil = new CommonUtil();

	public static double distInMetres(Double lat1, Double lng1, Double lat2, Double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2)
				+ Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist * 1609.34; // MIles to Metres
	}

	public static Date getEndDate(Long pUnitId, Date pBeginDate, Integer pUnitsCount, boolean isInclusive) {

		final Calendar c1 = Calendar.getInstance();
		c1.setTime(pBeginDate);

		if (pUnitId.intValue() == Constants.CONT_LOOKUP_PLAN_UNIT_MONTH) {
			c1.add(Calendar.MONTH, pUnitsCount);
		} else if (pUnitId.intValue() == Constants.CONT_LOOKUP_PLAN_UNIT_YEAR) {
			c1.add(Calendar.YEAR, pUnitsCount);
		} else if (pUnitId.intValue() == Constants.CONT_LOOKUP_PLAN_UNIT_DAY) {
			c1.add(Calendar.DATE, pUnitsCount);
		}

		if (isInclusive)
			c1.add(Calendar.DATE, -1);// -1 is to make the dates inclusive for
										// Plans
		return c1.getTime();
	}

	/*public static String processStripePayment(BigDecimal totalUnitsCost, String custId) throws StripeException {

		Stripe.apiKey = AppInitializer.stripeApiKey;
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("amount", CommonUtil.getStripeAmountFormat(totalUnitsCost));
		chargeMap.put("currency", "usd");
		chargeMap.put("customer", custId);
		
		 * Map<String, Object> cardMap = new HashMap<String, Object>();
		 * cardMap.put("number", this.getCardNumber()); cardMap.put("exp_month",
		 * expDate[0]); cardMap.put("exp_year", expDate[1]);
		 * cardMap.put("cvc_check", this.getCvv()); chargeMap.put("card",
		 * cardMap);
		 

		Charge charge = Charge.create(chargeMap);
		System.out.println(charge);
		return charge.getId();

	}*/

	/*public static String processStripePayment(BigDecimal totalUnitsCost, String cardNo, String expMonth, String expYear,
			String cvv) throws StripeException {

		Stripe.apiKey = AppInitializer.stripeApiKey;
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("amount", CommonUtil.getStripeAmountFormat(totalUnitsCost));
		chargeMap.put("currency", "usd");
		Map<String, Object> cardMap = new HashMap<String, Object>();
		cardMap.put("number", cardNo);
		cardMap.put("exp_month", expMonth);
		cardMap.put("exp_year", expYear);
		cardMap.put("cvc_check", cvv);
		chargeMap.put("card", cardMap);

		Charge charge = Charge.create(chargeMap);
		System.out.println(charge);
		return charge.getId();

	}

	public static String processStripePaymentV2(StripeDataTO pStripeDataTO) throws StripeException {

		Stripe.apiKey = AppInitializer.stripeApiKey;
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("amount", CommonUtil.getStripeAmountFormat(pStripeDataTO.getChargeAmount()));
		chargeMap.put("currency", "usd");
		if (pStripeDataTO.getCustomerId() == null) {
			Map<String, Object> cardMap = new HashMap<String, Object>();
			cardMap.put("number", pStripeDataTO.getCardNumber());
			cardMap.put("exp_month", pStripeDataTO.getExpMonth());
			cardMap.put("exp_year", pStripeDataTO.getExpYear());
			cardMap.put("cvc_check", pStripeDataTO.getCvv());
			chargeMap.put("card", cardMap);
		} else {
			chargeMap.put("customer", pStripeDataTO.getCustomerId());
		}

		Charge charge = Charge.create(chargeMap);
		System.out.println(charge);
		return charge.getId();

	}

	public static String createCustomer(String cardNo, String expMonth, String expYear, String cvv, String orgEmailId)
			throws RsntException {
		Stripe.apiKey = AppInitializer.stripeApiKey;
		Map<String, Object> defaultCardParams = new HashMap<String, Object>();
		Map<String, Object> defaultCustomerParams = new HashMap<String, Object>();

		// String[] expDate = this.getExpiryDate().split("-");

		defaultCardParams.put("number", cardNo);
		defaultCardParams.put("exp_month", expMonth);
		defaultCardParams.put("exp_year", expYear);
		defaultCardParams.put("cvc_check", cvv);

		defaultCustomerParams.put("card", defaultCardParams);
		defaultCustomerParams.put("description", orgEmailId);

		try {
			Customer cust = Customer.create(defaultCustomerParams);
			System.out.println(cust);
			return cust.getId();
		} catch (StripeException e) {
			e.printStackTrace();
			throw new RsntException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RsntException(e);
		}
	}

	public static Customer retrieveCustomer(String customerId) throws StripeException {

		Stripe.apiKey = AppInitializer.stripeApiKey;
		return Customer.retrieve(customerId);
	}

	public static boolean checkClientPromity(Double lat1, Double lng1, Double lat2, Double lng2) {
		if ((distInMetres(lat1, lng1, lat2, lng2) < 500))
			return true;
		return false;
	}

	public static String getStripeAmountFormat(BigDecimal cost) {
		BigDecimal test = cost.multiply(new BigDecimal(100));
		if (test.compareTo(new BigDecimal(0)) == -1) {
			test = test.negate();
		}
		NumberFormat num = NumberFormat.getInstance();
		num.setGroupingUsed(false);
		num.setMaximumFractionDigits(2);
		return num.format(test).toString();

	}*/

	/*public static JSONObject getJSONByGoogle(String fullAddress) throws RsntException {

		try {
			
			 * Create an java.net.URL object by passing the request URL in
			 * constructor. Here you can see I am converting the fullAddress
			 * String in UTF-8 format. You will get Exception if you don't
			 * convert your address in UTF-8 format. Perhaps google loves UTF-8
			 * format. :) In parameter we also need to pass "sensor" parameter.
			 * sensor (required parameter) — Indicates whether or not the
			 * geocoding request comes from a device with a location sensor.
			 * This value must be either true or false.
			 
			URL url = new URL(URL + "?address=" + URLEncoder.encode(fullAddress, "UTF-8") + "&sensor=false");

			// Open the Connection
			URLConnection conn = url.openConnection();

			// This is Simple a byte array output stream that we will use to
			// keep
			// the output data from google.
			ByteArrayOutputStream output = new ByteArrayOutputStream(1024);

			// copying the output data from Google which will be either in JSON
			// or
			// XML depending on your request URL that in which format you have
			// requested.
			IOUtils.copy(conn.getInputStream(), output);

			// close the byte array output stream now.
			output.close();

			
			 * if (output.toString().startsWith("{")) { StringBuffer newData =
			 * new StringBuffer(output.toString()); newData.append("]");
			 * newData.insert(0, "["); jsonData = newData.toString(); } else {
			 * jsonData = output.toString(); }
			 

			
			 * ObjectMapper mapper = new ObjectMapper();
			 * TypeReference<List<HashMap<String, String>>> typeRef = new
			 * TypeReference<List<HashMap<String, String>>>() { };
			 * List<HashMap<String, String>> updateData =
			 * mapper.readValue(jsonData, typeRef);
			 

			JSONObject jsonObject = new JSONObject(output.toString());
			String status = (String) jsonObject.get("status");

			if (!status.equalsIgnoreCase("OK")) {
				throw new RsntException("Unable to validate Address Details");
			}
			JSONArray arr = (JSONArray) jsonObject.get("results");
			if (arr.length() == 0) {
				throw new RsntException("Unable to validate Address Details");
			}
			jsonObject = (JSONObject) arr.get(0);
			String partialMatch = null;
			if (jsonObject.has("partial_match")) {
				partialMatch = jsonObject.getString("partial_match");
			}
			if (partialMatch != null) {
				String suggestedAddress = jsonObject.getString("formatted_address");
				throw new RsntException(suggestedAddress, Constants.CONT_ERR_ADDRESS_PARIAL_MATCH);
			}

			JSONObject jsonObjectfinal = (JSONObject) jsonObject.get("geometry");
			String locationType = jsonObjectfinal.getString("location_type");
			if (locationType.equalsIgnoreCase("APPROXIMATE")) {
				// throw new RsntException("Unable to validate Address
				// Details");
				throw new RsntException(null, Constants.CONT_ERR_ADDRESS_APPROX_MATCH);
			}

			jsonObjectfinal = (JSONObject) jsonObjectfinal.get("location");
			return jsonObjectfinal;// This returned String is JSON string from
									// which you can retrieve all key value pair
									// and can save it in POJO.
		} catch (RsntException e) {
			throw e;
		} catch (Exception e) {
			throw new RsntException(e.getMessage());
		}

	}*/

	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static Random rnd = new Random();

	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString() + convertDateToFormattedString(new Date(), "yyyyMMddhhmmss");
	}

	public static CommonUtil getInstance() {
		return cmnUtil;
	}

	/*public static String encryptPassword(String inputPassword) {

		// String sessionid = "password";
		byte[] defaultBytes = inputPassword.getBytes();
		// System.out.println("defaultBytes ::"+defaultBytes);
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			// algorithm.reset();
			algorithm.update(defaultBytes);
			byte[] messageDigest = algorithm.digest();

			String passwordHash = CryptoUtil.encodeBase16(messageDigest);
			// System.out.println("inputPassword "+inputPassword+" md5 version
			// is "+passwordHash.toString());
			return passwordHash;

			
			 * StringBuffer hexString = new StringBuffer(); for (int
			 * i=0;i<messageDigest.length;i++) {
			 * hexString.append(Integer.toHexString(0xFF & messageDigest[i])); }
			 * String foo = messageDigest.toString(); System.out.println(
			 * "inputPassword "+inputPassword+" md5 version is "
			 * +hexString.toString()); //System.out.println("foo "+foo);
			 * inputPassword=hexString+""; return inputPassword;
			 

			
			 * String passwordHash = CryptoUtil.createPasswordHash("MD5", "HEX",
			 * null, null, inputPassword);
			 

		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
		return null;
	}*/

	/**
	 * Convert the incoming object to String. If the object is null, return
	 * empty String.
	 * 
	 * @param obj
	 * @return String
	 */
	public static String toStr(final Object obj) {
		if (obj != null && !obj.equals("null")) {
			return obj.toString();
		} else {
			return "";
		}
	}

	public static String getYesNoFlag(final Object obj) {
		if (toStr(obj) != "") {
			String flag = toStr(obj);
			if (flag.equalsIgnoreCase("1") || flag.equalsIgnoreCase("true"))
				return "Y";
			else
				return "N";
		}
		return null;
	}

	/**
	 * Convert the incoming String[] to String. If the strArray is null, return
	 * empty String.
	 * 
	 * @param strArray
	 * @return String
	 */
	public static String convertStringArrayToString(final String[] strArray) {
		if (strArray != null && !strArray.equals("null")) {
			StringBuilder strBuilder = new StringBuilder();
			if (strArray instanceof String[] && strArray.length > 0) {
				for (String strObject : strArray) {
					strBuilder.append(strObject).append(",");
				}
			}
			return strBuilder.toString();
		} else {
			return "";
		}
	}

	public static String convertLongListToString(List<String> lgList1, List<String> lgList2) {
		StringBuilder strBuilder = new StringBuilder();
		if (lgList1 != null && lgList1.size() > 0) {
			for (String strObject : lgList1) {
				strBuilder.append(strObject.toString()).append("|");
			}
		}
		if (lgList2 != null && lgList2.size() > 0) {
			for (String strObject : lgList2) {
				strBuilder.append(strObject.toString()).append("|");
			}
		}
		return strBuilder.toString();

	}

	/**
	 * Checks if the Object is Null or Empty.
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isNullOrEmpty(final Object obj) {
		return obj == null;
	}

	/**
	 * Checks if <tt>obj</tt> is null or an empty. string or a string with only
	 * space (' ') character(s).
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isNullOrEmpty(final String obj) {
		String string = null;
		if (obj != null && obj != "null") {
			string = obj.trim();
		}
		return StringUtils.isEmpty(string);
	}

	/**
	 * Checks if <tt>obj1</tt> and <tt>obj2</tt> are equal or not.
	 * 
	 * @param obj1
	 *            , obj2
	 * @return boolean
	 */
	public static boolean areEqual(final Object obj1, final Object obj2) {
		return (obj1 == null && obj2 == null)
				|| (!CommonUtil.isNullOrEmpty(obj1) && !CommonUtil.isNullOrEmpty(obj2) && obj1.equals(obj2));
	}

	/**
	 * Checks if <tt>obj</tt> is null or zero.
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isNullOrZero(final Long obj) {
		return obj == null || obj.longValue() == 0;
	}

	/**
	 * Checks if <tt>obj</tt> is null or zero.
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isNullOrZero(final BigDecimal obj) {
		return obj == null || obj.longValue() == 0;
	}

	/**
	 * Checks if collection <tt>obj</tt> is null or empty.
	 * 
	 * @param obj
	 * @return boolean
	 */

	public static boolean isNullOrEmpty(final Collection obj) {
		// boolean isNullOrEmpty = false;
		return obj == null || obj.isEmpty();
	}

	/**
	 * Checks if array <tt>obj</tt> is null or of length zero.
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isNullOrEmpty(final Object[] obj) {
		return obj == null || obj.length == 0;
	}

	/**
	 * Checks if map <tt>obj</tt> is null or empty.
	 * 
	 * @param obj
	 * @return boolean
	 */

	public static boolean isNullOrEmpty(final Map obj) {
		return obj == null || obj.isEmpty();
	}

	/*
	 * This method searches a collection to find an object whose property
	 * matches propertyValue passed.
	 * 
	 * @param collection - to be searched
	 * 
	 * @param property - property to be compared (should have getter method) It
	 * can be a nested property in the form of name1.name2.name3
	 * 
	 * @param propertyValue - value to be compared
	 * 
	 * @return Object from the collection if found, otherwise null
	 * 
	 * @throws IllegalAccessException
	 * 
	 * @throws InvocationTargetException
	 * 
	 * @throws NoSuchMethodException
	 */

	public static <T> T findObject(final Collection<T> collection, final String property, final Object propertyValue)
			throws RsntException {
		T result = null;

		if (!CommonUtil.isNullOrEmpty(collection)) {
			try {
				for (final Object object : collection) {
					final Object objectPropertyValue = PropertyUtils.getNestedProperty(object, property);
					if (objectPropertyValue != null && objectPropertyValue.equals(propertyValue)) {
						result = (T) object;
						break;
					}
				}
			} catch (final IllegalAccessException illegalAccessException) {
				throw new RsntException(illegalAccessException);
			} catch (final InvocationTargetException invocationTargetException) {
				throw new RsntException(invocationTargetException);
			} catch (final NoSuchMethodException noSuchMethodException) {
				throw new RsntException(noSuchMethodException);
			}
		}
		return result;
	}

	/*
	 * This method searches a collection to find an object whose property
	 * matches propertyValue passed.
	 * 
	 * @param collection - to be searched
	 * 
	 * @param property - property to be compared (should have getter method) It
	 * can be a nested property in the form of name1.name2.name3
	 * 
	 * @param propertyValue - value to be compared
	 * 
	 * @return List<T>
	 * 
	 * @throws IllegalAccessException
	 * 
	 * @throws InvocationTargetException
	 * 
	 * @throws NoSuchMethodException
	 */

	public static <T> List<T> findObjects(final Collection<T> collection, final String property,
			final Object propertyValue) throws RsntException {
		final List<T> results = new ArrayList<T>();

		if (!CommonUtil.isNullOrEmpty(collection)) {
			try {
				for (final Object object : collection) {
					final Object objectPropertyValue = PropertyUtils.getNestedProperty(object, property);
					if (objectPropertyValue != null && objectPropertyValue.equals(propertyValue)) {
						results.add((T) object);
					}
				}
			} catch (final IllegalAccessException illegalAccessException) {
				throw new RsntException(illegalAccessException);
			} catch (final InvocationTargetException invocationTargetException) {
				throw new RsntException(invocationTargetException);
			} catch (final NoSuchMethodException noSuchMethodException) {
				throw new RsntException(noSuchMethodException);
			}
		}
		return results;
	}

	/*
	 * @param to be searched
	 * 
	 * @param property - property to be compared (should have getter method) It
	 * can be a nested property in the form of name1.name2.name3
	 * 
	 * @param propertyValue - value to be compared
	 * 
	 * @return true if found object with property found in the collection,
	 * otherwise false
	 * 
	 * @throws IllegalAccessException
	 * 
	 * @throws InvocationTargetException
	 * 
	 * @throws NoSuchMethodException
	 */

	public static boolean isObjectPresentInCollection(final Collection collection, final String property,
			final Object propertyValue) throws RsntException {

		return CommonUtil.findObject(collection, property, propertyValue) != null;
	}

	/*
	 * This method searches a collection to check the property is null for every
	 * object in collection.
	 * 
	 * @param to be searched
	 * 
	 * @param property - property to be compared (should have getter method) It
	 * can be a nested property in the form of name1.name2.name3
	 * 
	 * @param propertyValue - value to be compared
	 * 
	 * @return true if found object with property found in the collection,
	 * otherwise false
	 * 
	 * @throws IllegalAccessException
	 * 
	 * @throws InvocationTargetException
	 * 
	 * @throws NoSuchMethodException
	 */

	public boolean isPropertyNullInCollection(final Collection collection, final String property)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		boolean isAllPropertyValuesNull = true;
		if (!isNullOrEmpty(collection)) {
			for (final Object object : collection) {
				if (PropertyUtils.getNestedProperty(object, property) != null) {
					isAllPropertyValuesNull = false;
					break;
				}
			}
		}
		return isAllPropertyValuesNull;
	}

	/*
	 * This will sort the list in ascending based on comparing the value of the
	 * property name supplied. Returns false if something goes wrong, true
	 * otherwise.
	 */
	/*
	 * public static <T> boolean sortList(final List<T> listToSort, final String
	 * property, final boolean nullsAreHigh) { return sortList(listToSort,
	 * property, false, nullsAreHigh); }
	 * 
	 * 
	 * This will sort the list ascending / descending according to the
	 * descending flag and based on comparing the value of the property name
	 * supplied.
	 * 
	 * nullsAreHigh - If this parameter is true, then the null values in the
	 * list will appear before the non-null values after sorting. If this is
	 * false, reverse is true.
	 * 
	 * Returns false if something goes wrong, true otherwise.
	 * 
	 * public static <T> boolean sortList(final List<T> listToSort, final String
	 * property, final boolean isDescending, final boolean nullsAreHigh) { final
	 * StringBuffer status = new StringBuffer(); Collections.sort(listToSort,
	 * new Comparator<T>() {
	 * 
	 * @Override public int compare(final T o1, final T o2) { int result = 0;
	 * try { Object obj1 = PropertyUtils.getNestedProperty(o1, property); Object
	 * obj2 = PropertyUtils.getNestedProperty(o2, property);
	 *//**
		 * If any of the values is null, the compare shouldn't fail. It should
		 * sort the null values higher or lower than the actual values.
		 */

	/*
	 * if (obj1 != null && obj2 != null) { result = (Integer)
	 * Comparable.class.getDeclaredMethods()[0].invoke(obj1, obj2); } else if
	 * (obj1 == null && obj2 != null) { result = (nullsAreHigh == true) ? 1 :
	 * -1; } else if (obj1 != null && obj2 == null) { result = (nullsAreHigh ==
	 * true) ? -1 : 1; } else if (obj1 == null && obj2 == null) { result = 0; }
	 * if (isDescending) { result = result * -1; }
	 * 
	 * } catch (final IllegalAccessException e) { status.append(e.getMessage());
	 * } catch (final InvocationTargetException e) {
	 * status.append(e.getMessage()); } catch (final NoSuchMethodException e) {
	 * status.append(e.getMessage()); } return result; } }); return
	 * status.length() == 0; }
	 * 
	 * public static <T> boolean multiSortList(final List<T> listToSort, final
	 * List<Map<String,String>> sortingParameters, final boolean nullsAreHigh ,
	 * final String sortValue, final boolean isSortValueAtTop) { final
	 * StringBuffer status = new StringBuffer(); Collections.sort(listToSort,
	 * new Comparator<T>() {
	 * 
	 * @Override public int compare(final T o1, final T o2) { int result = 0;
	 * boolean applySort = true; try { for (Map<String,String> sortingParameter
	 * : sortingParameters) { final String property =
	 * sortingParameter.get("property"); final String direction =
	 * sortingParameter.get("direction"); Object obj1 =
	 * PropertyUtils.getNestedProperty(o1, property); Object obj2 =
	 * PropertyUtils.getNestedProperty(o2, property);
	 * 
	 * if (isSortValueAtTop && StringUtils.isNotBlank(sortValue) &&
	 * sortValue.equals(obj1.toString())) { applySort = false; obj1 = null; } if
	 * (isSortValueAtTop && StringUtils.isNotBlank(sortValue) &&
	 * sortValue.equals(obj2.toString())) { applySort = false; obj2 = null; }
	 *//**
		 * If any of the values is null, the compare shouldn't fail. It should
		 * sort the null values higher or lower than the actual values.
		 */
	/*
	 * if (obj1 != null && obj2 != null) { result = (Integer)
	 * Comparable.class.getDeclaredMethods()[0].invoke(obj1, obj2); } else if
	 * (obj1 == null && obj2 != null) { result = (nullsAreHigh == true) ? 1 :
	 * -1; } else if (obj1 != null && obj2 == null) { result = (nullsAreHigh ==
	 * true) ? -1 : 1; } else if (obj1 == null && obj2 == null) { result = 0; }
	 * if ("DESC".equals(direction)) { result = result * -1; }
	 * 
	 * if (result != 0) { break; } }
	 * 
	 * } catch (final IllegalAccessException e) { status.append(e.getMessage());
	 * } catch (final InvocationTargetException e) {
	 * status.append(e.getMessage()); } catch (final NoSuchMethodException e) {
	 * status.append(e.getMessage()); } return result; } }); return
	 * status.length() == 0; }
	 */

	/*
	 * This method fetches value of a property from an object in the collection
	 * and return a list of property values.
	 * 
	 * @param collection, which contains objects
	 * 
	 * @param property, which is to be read from the object in the collection.
	 * 
	 * @return list of property values
	 * 
	 * @throws IllegalAccessException
	 * 
	 * @throws InvocationTargetException
	 * 
	 * @throws NoSuchMethodException
	 */

	public static <T> List<T> getPropertyValuesInList(final Collection collection, final String property,
			final List<T> propertyValueList) throws RsntException {

		return (List<T>) CommonUtil.getPropertyValuesInCollection(collection, property, propertyValueList);
	}

	/*
	 * This method fetches value of a property from an object in the collection
	 * and return a set of property values.
	 * 
	 * @param collection, which contains objects
	 * 
	 * @param property, which is to be read from the object in the collection.
	 * 
	 * @return set of property values
	 * 
	 * @throws IllegalAccessException
	 * 
	 * @throws InvocationTargetException
	 * 
	 * @throws NoSuchMethodException
	 */

	public static <T> Set<T> getPropertyValuesInSet(final Collection collection, final String property,
			final Set<T> propertyValueSet) throws RsntException {

		return (Set<T>) CommonUtil.getPropertyValuesInCollection(collection, property, propertyValueSet);
	}

	/*
	 * This method fetches value of a property from an object in the collection
	 * and return a collection of property values.
	 * 
	 * @param collection, which contains objects
	 * 
	 * @param property, which is to be read from the object in the collection.
	 * 
	 * @return collection of property values
	 * 
	 * @throws IllegalAccessException
	 * 
	 * @throws InvocationTargetException
	 * 
	 * @throws NoSuchMethodException
	 */

	public static <T> Collection<T> getPropertyValuesInCollection(final Collection collection, final String property,
			final Collection<T> propertyValueCollection) throws RsntException {
		try {
			if (!CommonUtil.isNullOrEmpty(collection)) {
				for (final Object object : collection) {
					Object obj;
					obj = PropertyUtils.getNestedProperty(object, property);
					if (obj != null) {
						propertyValueCollection.add((T) obj);
					}
				}
			}
		} catch (final IllegalAccessException illegalAccessException) {
			throw new RsntException(illegalAccessException);
		} catch (final InvocationTargetException invocationTargetException) {
			throw new RsntException(invocationTargetException);
		} catch (final NoSuchMethodException noSuchMethodException) {
			throw new RsntException(noSuchMethodException);
		}
		return propertyValueCollection;
	}

	/**
	 * Returns Date into String into the format specified.
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String convertDateToFormattedString(final Date date, final String format) {
		final SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		String str = null;
		if (date != null) {
			str = sdf.format(date);
		}
		return str;
	}

	public static String convertTo24HoursFormat(final String twelveHourTime) throws ParseException {
		DateFormat TWELVE_TF = new SimpleDateFormat("hh:mm a"); // Replace with
																// kk:mm if you
																// want 1-24
																// interval
		DateFormat TWENTY_FOUR_TF = new SimpleDateFormat("HH:mm");
		return TWENTY_FOUR_TF.format(TWELVE_TF.parse(twelveHourTime));
	}

	/**
	 * Returns Date by converting the date string in the format specified.
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStringToDate(final String dateStr, final String format) {

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String input = dateStr;
		ParsePosition pp = new ParsePosition(0);
		Date d = null;
		try {
			d = formatter.parse(input);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * Returns true if date range imputted is valid.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean isDateRangeValid(final Date from, final Date to) {
		return !CommonUtil.isNullOrEmpty(from) && !CommonUtil.isNullOrEmpty(to) && to.after(from);
	}

	/**
	 * Returns String with Escape sequence for Wild Cards.
	 * 
	 * @param condition
	 * @return
	 */
	public static String addEscSeqForWildCard(final String condition) {
		if (condition != null) {
			return condition.replaceAll("_", "\\\\_").replaceAll("%", "\\\\%");
		} else {
			return condition;
		}
	}

	/**
	 * Returns the required Entity
	 * 
	 * @param <Entity>
	 * @param entityManager
	 * @param entityClass
	 * @param entityId
	 * @return
	 */
	public static <Entity> Entity findEntity(final EntityManager entityManager, final Class<Entity> entityClass,
			final Object entityId) {
		return entityManager.find(entityClass, entityId);
	}

	/**
	 * Returns whether the entity is dirty or not.
	 * 
	 * @param entityManager
	 * @return
	 */
	public static boolean isDirty(final EntityManager entityManager) {
		return ((Session) entityManager.getDelegate()).isDirty();
	}

	/**
	 * Returns the evicted entity.
	 * 
	 * @param <Entity>
	 * @param entityManager
	 * @param entity
	 * @return
	 */
	public static <Entity> Entity evictEntity(final EntityManager entityManager, final Entity entity) {
		if (entityManager.contains(entity)) {
			((Session) entityManager.getDelegate()).evict(entity);
		}
		return entity;
	}

	/**
	 * Returns the Queried Entity.
	 * 
	 * @param <Entity>
	 * @param entityManager
	 * @param entityClass
	 * @param fieldConditions
	 * @param constructorFields
	 * @return
	 */

	public static <Entity> List<Entity> queryEntity(final EntityManager entityManager, final Class<Entity> entityClass,
			final Map<String, Object> fieldConditions, final String... constructorFields) {
		final StringBuffer queryBuffer = new StringBuffer(40);
		queryBuffer.append("SELECT ");
		if (constructorFields.length == 0) {
			queryBuffer.append("e ");
		} else {
			queryBuffer.append("new ");
			queryBuffer.append(entityClass.getSimpleName());
			queryBuffer.append(" ( ");
			for (int index = 0; index < constructorFields.length; index++) {
				queryBuffer.append("e.");
				queryBuffer.append(constructorFields[index]);
				if (index < constructorFields.length - 1) {
					queryBuffer.append(", ");
				}
			}
			queryBuffer.append(" ) ");
		}
		queryBuffer.append("FROM ");
		queryBuffer.append(entityClass.getSimpleName());
		queryBuffer.append(" e WHERE ");
		final Iterator<String> fields = fieldConditions.keySet().iterator();
		while (fields.hasNext()) {
			final String field = fields.next();
			queryBuffer.append("e.");
			queryBuffer.append(field);
			queryBuffer.append(" = :");
			queryBuffer.append(field);
			if (fields.hasNext()) {
				queryBuffer.append(" AND ");
			} else {
				queryBuffer.append(" ORDER BY e.");
				queryBuffer.append(field);
			}
		}
		final Query query = entityManager.createQuery(queryBuffer.toString());
		for (final String field : fieldConditions.keySet()) {
			query.setParameter(field, fieldConditions.get(field));
		}
		return query.getResultList();
	}

	/**
	 * Returns Entity Queried by ID.
	 * 
	 * @param <Entity>
	 * @param entityManager
	 * @param entityClass
	 * @param id
	 * @param constructorFields
	 * @return
	 */
	public static <Entity> Entity queryEntityById(final EntityManager entityManager, final Class<Entity> entityClass,
			final Long id, final String... constructorFields) {
		Entity entity = null;
		final Map<String, Object> fieldConditions = new HashMap<String, Object>();
		fieldConditions.put("id", id);
		final List<Entity> entityList = queryEntity(entityManager, entityClass, fieldConditions, constructorFields);
		if (!entityList.isEmpty()) {
			entity = entityList.get(0);
		}
		return entity;
	}

	/**
	 * Refreshes the Entity.
	 * 
	 * @param <Entity>
	 * @param entityManager
	 * @param entity
	 * @return
	 * @throws RsntException
	 */

	public static <Entity> Entity refreshEntity(final EntityManager entityManager, final Entity entity)
			throws RsntException {
		try {
			if (entityManager.contains(entity)) {
				entityManager.refresh(entity);
				return entity;
			} else {
				return (Entity) entityManager.find(entity.getClass(),
						Long.parseLong(BeanUtils.getProperty(entity, "id")));
			}
		} catch (final Exception e) {
			throw new RsntException(e);
		}
	}

	/**
	 * Validate Regular Expression check for input fieldValue.
	 * 
	 * @param regExString
	 * @param fieldValue
	 * @return
	 */
	public static boolean isRegExCheckValid(final String regExString, final String fieldValue) {
		return Pattern.matches(regExString, fieldValue);
	}

	/**
	 * Determine the difference between two dates and returns no of days.
	 * 
	 * @param date1
	 * @Date
	 * @param date2
	 * @Date
	 * @return int
	 */
	public static int getDateDifferenceInDay(final Date date1, final Date date2) {
		int dtDiff = 0;
		if (date1 == null) {
			return dtDiff;
		}
		final String dateDiff = String.valueOf((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000));
		if (dateDiff != null) {
			dtDiff = Integer.parseInt(dateDiff);
		}
		return dtDiff;
	}

	/**
	 * Returns the next day date of todays date
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date getNextDay(final Date date) {
		final Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.DATE, 1);
		return c1.getTime();
	}

	public static Date getPreviousDay(final Date date) {
		final Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.DATE, -1);
		return c1.getTime();
	}

	public static Date getFirstDayOfNextMonth(final Date date) {
		final Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.MONTH, 1);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		return c1.getTime();
	}

	public static Date getLastDayOfMonth() {
		final Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date());

		c1.add(Calendar.MONTH, 1);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		c1.add(Calendar.DATE, -1);

		return c1.getTime();
	}

	public static Date getFirstDayOfMonth() {
		final Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date());
		c1.set(Calendar.DAY_OF_MONTH, 1);
		return c1.getTime();
	}

	/**
	 * Returns the formatted date in dd-MMM-yyyy date
	 * 
	 * @param date
	 * @return String
	 */
	/*
	 * public static String dateFormattor(final Date date) { if (date != null) {
	 * final Format formatter = new SimpleDateFormat(Constants.DATE_FORMATOR,
	 * Locale.getDefault()); return formatter.format(date); } else { return
	 * null; } }
	 */

	/**
	 * Returns the Next Term Year End Date based on Start Date in MM-dd-yyyy
	 * formate
	 * 
	 * @param startDate
	 * @Date
	 * @return @Date
	 */
	public static Date getNextTermYearDate(final Date startDate) throws ParseException {
		final Calendar c1 = Calendar.getInstance();
		c1.setTime(startDate);
		c1.add(Calendar.MONTH, 12);
		c1.add(Calendar.DATE, -1);

		final int month = c1.get(Calendar.MONTH) + 1;
		final int sdate = c1.get(Calendar.DATE);
		final int year = c1.get(Calendar.YEAR);

		final String tempStartDate = month + "-" + sdate + "-" + year;
		final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

		return sdf.parse(tempStartDate);
	}

	/**
	 * Returns the Set containing extra elements in newList apart from oldList
	 * 
	 * @param newList
	 * @param oldList
	 * @return addSuperset - @Set<@String>
	 */
	public static Set<String> getNewSet(final Collection<String> newList, final Collection<String> oldList) {

		final Set<String> addSuperset = new TreeSet<String>();
		addSuperset.addAll(newList);
		addSuperset.addAll(oldList);
		addSuperset.removeAll(oldList);
		return addSuperset;
	}

	/**
	 * Returns the Set containing elements that are removed from the newList but
	 * present in oldList
	 * 
	 * @param @Collection<@String>
	 *            - newList
	 * @param @Collection<@String>
	 *            - oldList
	 * @return addSuperset - @Set<@String>
	 */
	public static Set<String> getDelSet(final Collection<String> newList, final Collection<String> oldList) {

		final Set<String> addSuperset = new TreeSet<String>();
		addSuperset.addAll(newList);
		addSuperset.addAll(oldList);
		addSuperset.removeAll(newList);
		return addSuperset;
	}

	public static Set<Long> addSuperSet(final Collection<Long> newList, final Collection<Long> oldList) {
		final Set<Long> addSuperset = new TreeSet<Long>();
		addSuperset.addAll(newList);
		addSuperset.addAll(oldList);
		addSuperset.removeAll(oldList);
		return addSuperset;
	}

	public static Set<Long> removeSuperSet(final Collection<Long> newList, final Collection<Long> oldList) {
		final Set<Long> addSuperset = new TreeSet<Long>();
		addSuperset.addAll(newList);
		addSuperset.addAll(oldList);
		addSuperset.removeAll(newList);
		return addSuperset;
	}

	/**
	 * Gets the filter JSON string from UI and converts it into collection of
	 * filter data.
	 * 
	 * @param @String
	 *            filterJSON
	 * @return @List<@Map<@String,@String>>
	 */

	public static String fetchDefaultSortingInfo(final String jsonString) {
		if (!isNullOrEmpty(jsonString) && !"null".equals(jsonString)) {
			JSONArray jsonArray = convertJSONStrToJSONArray(jsonString);
			if (jsonArray != null) {
				Iterator<?> iterator = jsonArray.iterator();
				while (iterator.hasNext()) {
					JSONObject obj = JSONObject.fromObject(iterator.next());
					if (StringUtils.isNotBlank(obj.getString("sortingInfo"))
							&& !"null".equals(obj.getString("sortingInfo"))) {
						return obj.getString("sortingInfo");
					} else {
						return "";
					}
				}
			}
		}
		return "";
	}

	public static Map<String, String> fetchSortingInformation(JSONArray jsonArray) {
		Map<String, String> sortInfo = new HashMap<String, String>();
		if (jsonArray != null) {
			Iterator<?> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				JSONObject obj = JSONObject.fromObject(iterator.next());
				sortInfo.put("property", obj.getString("property"));
				sortInfo.put("direction", obj.getString("direction"));
			}
		}
		return sortInfo;
	}

	public static List<Map<String, String>> fetchMultiSortingInformation(JSONArray jsonArray) {
		List<Map<String, String>> sortInfoList = new ArrayList<Map<String, String>>();
		if (jsonArray != null) {
			Iterator<?> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				Map<String, String> sortInfo = new HashMap<String, String>();
				JSONObject obj = JSONObject.fromObject(iterator.next());
				sortInfo.put("property", obj.getString("property"));
				sortInfo.put("direction", obj.getString("direction"));
				sortInfoList.add(sortInfo);
			}
		}
		return sortInfoList;
	}

	public static List<Long> fetchDealProductIdsFromJsonString(final String jsonString) {
		List<Long> dealProductIdsList = new ArrayList<Long>();
		if (!isNullOrEmpty(jsonString)) {
			JSONArray jsonArray = convertJSONStrToJSONArray(jsonString);
			if (jsonArray != null) {
				Iterator<?> iterator = jsonArray.iterator();
				while (iterator.hasNext()) {
					JSONObject obj = JSONObject.fromObject(iterator.next());
					dealProductIdsList.add(Long.valueOf(obj.getLong("dealProductId")));
				}
			}
		}
		return dealProductIdsList;
	}

	public static List<Long> fetchBudgetIdsFromJsonString(final String jsonString) {
		List<Long> dealProductIdsList = new ArrayList<Long>();
		if (!isNullOrEmpty(jsonString)) {
			JSONArray jsonArray = convertJSONStrToJSONArray(jsonString);
			if (jsonArray != null) {
				Iterator<?> iterator = jsonArray.iterator();
				while (iterator.hasNext()) {
					JSONObject obj = JSONObject.fromObject(iterator.next());
					dealProductIdsList.add(Long.valueOf(obj.getLong("madjBudjId")));
				}
			}
		}
		return dealProductIdsList;
	}

	/**
	 * Converts a JSONString into a JSONArray
	 * 
	 * @param JsonString
	 * @return JSONArray
	 */
	public static JSONArray convertJSONStrToJSONArray(String JsonString) {
		if (!CommonUtil.isNullOrEmpty(JsonString)) {
			if (JsonString.startsWith("[")) {
				return JSONArray.fromObject(JsonString);
			} else {
				JSONObject jObject = JSONObject.fromObject(JsonString);
				return JSONArray.fromObject(jObject);
			}
		}
		return null;
	}

	/**
	 * The following method is added by Punit Oza to check if the date is a
	 * Perpetuity date.
	 */
	public static boolean isPerpetuityDate(Date date) {
		if (date.toString().equals(getPerpetuityDate())) {
			return true;
		}
		return false;
	}

	/**
	 * The following method is added by Punit Oza to get the Perpertuity Date
	 * instance.
	 */
	public static Date getPerpetuityDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(9999, 11, 31, 0, 0, 0);
		return cal.getTime();
	}

	public static Object processData(Object obj, String methodName) throws RsntException {
		try {
			Class<?> c = obj.getClass();
			Method method = c.getDeclaredMethod(methodName);
			return method.invoke(obj);
		} catch (SecurityException e) {
			final String errorMsg = "ExportExcelService.processData : Error while accessing the method on object. Method name "
					+ methodName + " on Class : " + obj.getClass().getName();
			throw new RsntException(errorMsg, e);
		} catch (NoSuchMethodException e) {
			final String errorMsg = "ExportExcelService.processData : Error while accessing the method on object. Method name "
					+ methodName + " on Class : " + obj.getClass().getName();
			throw new RsntException(errorMsg, e);
		} catch (IllegalAccessException e) {
			final String errorMsg = "ExportExcelService.processData : Error while accessing the method on object. Method name "
					+ methodName + " on Class : " + obj.getClass().getName();
			throw new RsntException(errorMsg, e);
		} catch (IllegalArgumentException e) {
			final String errorMsg = "ExportExcelService.processData : Error while accessing the method on object. Method name "
					+ methodName + " on Class : " + obj.getClass().getName();
			throw new RsntException(errorMsg, e);
		} catch (InvocationTargetException e) {
			final String errorMsg = "ExportExcelService.processData : Error while accessing the method on object. Method name "
					+ methodName + " on Class : " + obj.getClass().getName();
			throw new RsntException(errorMsg, e);
		}
	}

	public static List<String> splitStringToList(final String string) {
		List<String> stringList = new ArrayList<String>();
		if (!CommonUtil.isNullOrEmpty(string)) {
			for (String tempNoteId : string.split(",")) {
				stringList.add(tempNoteId);
			}
			return stringList;
		}
		return null;
	}

	public static List<String> splitStringToList(final String string, String splitChar) {
		List<String> stringList = new ArrayList<String>();
		if (!CommonUtil.isNullOrEmpty(string)) {
			for (String tempNoteId : string.split(splitChar)) {
				stringList.add(tempNoteId);
			}
			return stringList;
		}
		return null;
	}

	public static List<Long> splitStringToListOfLong(final String valuesInString, String separator) {
		List<Long> longList = new ArrayList<Long>();
		if (!CommonUtil.isNullOrEmpty(valuesInString)) {
			final StringTokenizer st = new StringTokenizer(valuesInString, separator);
			while (st.hasMoreTokens()) {
				final String value = st.nextToken();
				longList.add(Long.valueOf(value));
			}
			return longList;
		}
		return null;
	}

	public static String formatDoubleValues(final String value) {
		if (value != "0.0") {
			NumberFormat cfLocal = NumberFormat.getCurrencyInstance(Locale.US);
			DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) cfLocal).getDecimalFormatSymbols();
			decimalFormatSymbols.setCurrencySymbol("");
			((DecimalFormat) cfLocal).setDecimalFormatSymbols(decimalFormatSymbols);
			String formattedString = cfLocal.format(Double.valueOf(value)).trim();
			return formattedString;
		} else {
			return "0";
		}
	}

	/*public void fetchFileOnServer(String templateType) {
		String fileName = "";

		if (templateType.equalsIgnoreCase("TITLES"))
			fileName = "Deal_Titles_Import_Template.xls";
		else if (templateType.equalsIgnoreCase("BUDGET"))
			fileName = "import_template_unassociated_revenue_budget.xls";

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		File file = new File(classLoader.getResource(fileName).getPath());
		System.out.println(file);

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		try {
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("content-disposition", "attachment; filename= " + fileName);
			// file.write(response.getOutputStream());
			// response.getOutputStream().write(file);

			FileInputStream fileInput = new FileInputStream(file);
			int numOfBytes = fileInput.available();
			byte byteArray[] = new byte[numOfBytes];
			fileInput.read(byteArray);
			fileInput.close();

			OutputStream outStream = response.getOutputStream();
			outStream.write(byteArray);
			response.flushBuffer();
			response.getOutputStream().close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	

	/*public static void downloadFile(String fileName, byte[] file, String contentType) throws RsntException {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setContentType(contentType);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		response.setHeader("cache-control", "no-cache");
		ServletOutputStream outs;
		try {
			outs = response.getOutputStream();
			outs.write(file);
			outs.flush();
			outs.close();
			facesContext.responseComplete();
		} catch (IOException e) {
			throw new RsntException("Error while downloading the file", e);
		}
	}*/

	public static PushNotificationPayload getJSONPushMessageIphone(String message, String optionId,
			String messageString) {

		PushNotificationPayload payload = PushNotificationPayload.complex();
		try {
			final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
			final List<Object> dataArray = new ArrayList<Object>(0);

			
			/*rootMap.put("caption", message);
			rootMap.put("feature", "Alert");
			rootMap.put("message", "Your request will be attended shortly");
			rootMap.put("Id", optionId);
			rootMap.put("success", 1);
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			System.out.println(jsonObject.toString());*/
			 

			payload.addCustomDictionary("caption", message);
			payload.addCustomDictionary("feature", "Alert");
			payload.addCustomDictionary("message", messageString);
			// payload.addCustomDictionary("message", " Your
			// request:\n\""+message+"\" \nhas been received.");
			payload.addCustomDictionary("Id", optionId);
			payload.addCustomDictionary("success", 1);
			System.out.println(payload);

		} catch (Exception e) {
			System.out.println(e + " inside the getJSONPushMessage method");

		}
		return payload;

	}

	public static PushNotificationPayload getJSONPushMessageIphone(String message, String messageString) {

		PushNotificationPayload payload = PushNotificationPayload.complex();
		try {
			final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
			final List<Object> dataArray = new ArrayList<Object>(0);

			
			/*rootMap.put("caption", message);
			rootMap.put("feature", "Alert");
			rootMap.put("message", "Your request will be attended shortly");
			rootMap.put("Id", optionId);
			rootMap.put("success", 1);
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			System.out.println(jsonObject.toString());*/
			 

			payload.addCustomDictionary("caption", message);
			payload.addCustomDictionary("feature", "Alert");
			payload.addCustomDictionary("message", messageString);
			// payload.addCustomDictionary("message", " Your
			// request:\n\""+message+"\" \nhas been received.");
			payload.addCustomDictionary("success", 1);
			System.out.println(payload);

		} catch (Exception e) {
			System.out.println(e + " inside the getJSONPushMessage method");

		}
		return payload;

	}

	public static String getJSONPushMessageAndroid(String message, String optionId, String messageString) {
		String returnString = null;
		try {
			final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
			final List<Object> dataArray = new ArrayList<Object>(0);

			rootMap.put("caption", message);
			rootMap.put("feature", "Alert");
			rootMap.put("message", messageString);
			// rootMap.put("message", " Your request:\n\""+message+"\" \nhas
			// been received");
			rootMap.put("Id", optionId);
			rootMap.put("success", 1);
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			System.out.println(jsonObject.toString());
			returnString = jsonObject.toString();

		} catch (Exception e) {
			System.out.println(e + " inside the getJSONPushMessage method");

		}
		return returnString;

	}

	public static String getJSONPushMessageAndroid(String message, String messageString) {
		String returnString = null;
		try {
			final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
			final List<Object> dataArray = new ArrayList<Object>(0);

			rootMap.put("caption", message);
			rootMap.put("feature", "Alert");
			rootMap.put("message", messageString);
			// rootMap.put("message", " Your request:\n\""+message+"\" \nhas
			// been received");
			rootMap.put("success", 1);
			final JSONObject jsonObject = JSONObject.fromObject(rootMap);
			System.out.println(jsonObject.toString());
			returnString = jsonObject.toString();

		} catch (Exception e) {
			System.out.println(e + " inside the getJSONPushMessage method");

		}
		return returnString;

	}

	public static String convertWeekNumberToString(String weekNumberString) {

		weekNumberString = weekNumberString.replace("2", "Monday");
		weekNumberString = weekNumberString.replace("3", "Tuesday");
		weekNumberString = weekNumberString.replace("4", "Wednesday");
		weekNumberString = weekNumberString.replace("5", "Thursday");
		weekNumberString = weekNumberString.replace("6", "Friday");
		weekNumberString = weekNumberString.replace("7", "Saturday");
		weekNumberString = weekNumberString.replace("1", "Sunday");

		weekNumberString = weekNumberString.replace("|", ",");
		weekNumberString = weekNumberString.substring(0, weekNumberString.length() - 1);
		return weekNumberString;
	}

	public static String convertDateStrToFormattedDateStr(final String dateStr, final String srcFmt,
			final String targetFmt) {

		SimpleDateFormat srcFormatter = new SimpleDateFormat(srcFmt);
		SimpleDateFormat targetFormatter = new SimpleDateFormat(targetFmt);
		String input = dateStr;
		ParsePosition pp = new ParsePosition(0);
		Date d = srcFormatter.parse(input, pp);

		return targetFormatter.format(d);

	}

	public static void main(String[] args) {
		// System.out.println(convertDateToFormattedString(new Date(),
		// "yyyyMMddhhmmss"));
		// System.out.println(encryptPassword("test47"));
		/*
		 * AdUsageWrapperTO adUsageWrapperTO = new AdUsageWrapperTO();
		 * 
		 * List<AdUsageHistoryReportTO> adUsageHistoryReportSet = new
		 * ArrayList<AdUsageHistoryReportTO>();
		 * 
		 * AdUsageHistoryReportTO adUsageHistoryReport3 = new
		 * AdUsageHistoryReportTO(); adUsageHistoryReport3.setAdsBalance(new
		 * Long(300)); adUsageHistoryReport3.setAdsCredit(new Long(301));
		 * adUsageHistoryReport3.setActivityDesc("Beginning Balance");
		 * adUsageHistoryReport3.setActivityDate(convertDateToFormattedString(
		 * CommonUtil.getFirstDayOfNextMonth(new Date()), "yyyy-MM-dd"));
		 * adUsageHistoryReportSet.add(adUsageHistoryReport3);
		 * 
		 * AdUsageHistoryReportTO adUsageHistoryReport = new
		 * AdUsageHistoryReportTO(); adUsageHistoryReport.setAdsBalance(new
		 * Long(100)); adUsageHistoryReport.setAdsCredit(new Long(101));//Ads
		 * Consumed: Total As before - Current Org Ad balance
		 * adUsageHistoryReport.setActivityDesc(
		 * "Current Plan Ads Usage for Current Month");
		 * adUsageHistoryReport.setActivityDate(convertDateToFormattedString(new
		 * Date(), "yyyy-MM-dd"));
		 * adUsageHistoryReportSet.add(adUsageHistoryReport);
		 * 
		 * AdUsageHistoryReportTO adUsageHistoryReport2 = new
		 * AdUsageHistoryReportTO(); adUsageHistoryReport2.setAdsBalance(new
		 * Long(200)); adUsageHistoryReport2.setAdsCredit(new Long(201));
		 * adUsageHistoryReport2.setActivityDesc("Ending Balance");
		 * adUsageHistoryReport2.setActivityDate(convertDateToFormattedString(
		 * new Date(), "yyyy-MM-dd"));
		 * adUsageHistoryReportSet.add(adUsageHistoryReport2);
		 * 
		 * adUsageWrapperTO.setAdUsageHistoryReportTOList(
		 * adUsageHistoryReportSet);
		 * 
		 * createPdfDocument(null, adUsageWrapperTO);
		 */

		new CommonUtil().generateQRCodeImageWithDesc();
	}

	public static void createPdfDocument(OutputStream outputStream, AdUsageWrapperDTO adUsageWrapperTO) {
		String[] headers = new String[] { "Date", "Activity", "#Ads", "Ad Balance", "Amount", "Notes" };
		/*
		 * String[][] data = new String[][] { { "1", "JDOW", "JOHN", "DOW" }, {
		 * "2", "STIGER", "SCOTT", "TIGER" }, { "3", "FBAR", "FOO", "BAR" } };
		 */

		//
		// Create a new document.
		//
		Document document = new Document(PageSize.LETTER.rotate());

		try {
			//
			// Get an instance of PdfWriter and create a Table.pdf file as an
			// output.
			//
			PdfWriter.getInstance(document, outputStream);
			// PdfWriter.getInstance(document, new FileOutputStream(new
			// File("C:\\Vish\\Table.pdf")));
			document.open();

			//
			// Create an instance of PdfPTable. After that we transform the
			// header and
			// data array into a PdfPCell object. When each table row is
			// complete we
			// have to call the table.completeRow() method.
			//
			// For better presentation we also set the cell font name, size and
			// weight.
			// And we also define the background fill for the cell.
			//

			// Paragraph paragraph = new Paragraph();
			// paragraph.add(new Chunk("MONTHLY STATEMENT"));
			// paragraph.add(new Chunk("10/01/2013 - 10/31/2013"));

			// document.add(paragraph);

			PdfPTable tableParag = new PdfPTable(1);
			tableParag.setWidthPercentage(100);
			tableParag.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			tableParag.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			tableParag.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			tableParag.getDefaultCell().setFixedHeight(40);
			tableParag.addCell(new Phrase("MONTHLY STATEMENT", new Font(Font.HELVETICA, 15, Font.BOLD)));
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(new Date());
			startDate.set(Calendar.DAY_OF_MONTH, 1);

			Calendar endDate = Calendar.getInstance();
			startDate.setTime(new Date());
			startDate.set(Calendar.DAY_OF_MONTH, 31);

			tableParag.addCell(new Phrase(adUsageWrapperTO.getDateSpan(), new Font(Font.HELVETICA, 15, Font.BOLD)));

			document.add(tableParag);

			PdfPTable tableAddress = new PdfPTable(1);
			tableAddress.setWidthPercentage(80);
			tableAddress.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			tableAddress.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			tableAddress.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			tableAddress.getDefaultCell().setFixedHeight(30);

			// tableAddress.addCell("Restaurant Info: TGIF, Store #1574, 1487
			// Seville Ave., Unit 147, Los Angeles, CA 90076");
			tableAddress.addCell("Restaurant Info: " + adUsageWrapperTO.getAddress());

			// Cell cellAdd = new Cell("TGIF, Store #1574, 1487 Seville Ave.,
			// Unit 147, Los Angeles, CA 90076");
			// cellAdd.setWidth(50);
			// tableAddress.add(cellAdd);
			// tableAddress.addCell("");

			tableAddress.addCell("Current Plan:     " + adUsageWrapperTO.getPlanName());
			// tableAddress.addCell("");

			document.add(tableAddress);

			PdfPTable table = new PdfPTable(headers.length);
			float[] widths = { 5f, 15f, 2f, 4f, 4f, 5f };
			table.setWidths(widths);
			// document.add(table);

			for (int i = 0; i < headers.length; i++) {
				String header = headers[i];
				PdfPCell cellHeader = new PdfPCell();
				cellHeader.setGrayFill(0.9f);
				cellHeader.setPhrase(new Phrase(header.toUpperCase(), new Font(Font.HELVETICA, 10, Font.BOLD)));
				table.addCell(cellHeader);
			}
			table.completeRow();

			/*
			 * for (int i = 0; i < data.length; i++) { for (int j = 0; j <
			 * data[i].length; j++) { String datum = data[i][j]; PdfPCell cell =
			 * new PdfPCell(); cell.setPhrase(new Phrase(datum.toUpperCase(),
			 * new Font( Font.HELVETICA, 10, Font.NORMAL)));
			 * table.addCell(cell); } table.completeRow(); }
			 */

			List<AdUsageHistoryReportDTO> adUsageHistoryReportTOList = adUsageWrapperTO.getAdUsageHistoryReportTOList();
			for (AdUsageHistoryReportDTO reportTO : adUsageHistoryReportTOList) {
				PdfPCell cell = new PdfPCell();
				cell.setPhrase(new Phrase(reportTO.getActivityDate(), new Font(Font.HELVETICA, 12, Font.NORMAL)));
				table.addCell(cell);

				cell = new PdfPCell();
				cell.setPhrase(new Phrase(reportTO.getActivityDesc(), new Font(Font.HELVETICA, 12, Font.NORMAL)));
				cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
				cell.setNoWrap(true);
				table.addCell(cell);

				cell = new PdfPCell();
				if (reportTO.getAdsCredit() != null)
					cell.setPhrase(
							new Phrase(reportTO.getAdsCredit().toString(), new Font(Font.HELVETICA, 12, Font.NORMAL)));
				else
					cell.setPhrase(new Phrase(""));
				table.addCell(cell);

				cell = new PdfPCell();
				if (reportTO.getAdsBalance() != null)
					cell.setPhrase(
							new Phrase(reportTO.getAdsBalance().toString(), new Font(Font.HELVETICA, 12, Font.NORMAL)));
				else
					cell.setPhrase(new Phrase(""));
				table.addCell(cell);

				cell = new PdfPCell();
				String amt = reportTO.getAmount() != null ? reportTO.getAmount().toString() : "";
				cell.setPhrase(new Phrase(amt, new Font(Font.HELVETICA, 12, Font.NORMAL)));
				table.addCell(cell);

				cell = new PdfPCell();
				cell.setPhrase(new Phrase(reportTO.getNotes(), new Font(Font.HELVETICA, 12, Font.NORMAL)));
				table.addCell(cell);

				table.completeRow();
			}

			document.addTitle("Monthly Report");

			document.add(table);

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			document.close();
		}
	}

	private byte[] generateQRCodeImageWithDesc() {
		ByteArrayOutputStream boe = null;
		ByteArrayOutputStream boeOutput = null;
		Charset charset = Charset.forName("UTF-8");
		CharsetEncoder encoder = charset.newEncoder();
		byte[] b = null;
		try {
			// Convert a string to UTF-8 bytes in a ByteBuffer
			// ByteBuffer bbuf =
			// encoder.encode(CharBuffer.wrap("MECARD:N:test;ORG:test;TEL:2222;URL:www.google.com;EMAIL:ss@ss.com;ADR:r
			// r;NOTE:rtest;;"));
			ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(String.valueOf(+1)));
			b = bbuf.array();
		} catch (CharacterCodingException e) {
			System.out.println(e.getMessage());
		}

		String data;
		try {
			data = new String(b, "UTF-8");
			// get a byte matrix for the data
			BitMatrix matrix = null;
			int h = 144;
			int w = 144;
			com.google.zxing.Writer writer = new MultiFormatWriter();
			try {
				Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>(2);
				hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
				hints.put(EncodeHintType.MARGIN, 1);
				matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, w, h, hints);
			} catch (com.google.zxing.WriterException e) {
				System.out.println(e.getMessage());
			}

			// change this path to match yours (this is my mac home folder, you
			// can use: c:\\qr_png.png if you are on windows)
			// String filePath = "C:/Vish/qr_png.png";
			// File file = new File(filePath);
			try {
				// MatrixToImageWriter.writeToFile(matrix, "PNG", file);
				boe = new ByteArrayOutputStream();
				MatrixToImageWriter.writeToStream(matrix, "PNG", boe);
				InputStream in = new ByteArrayInputStream(boe.toByteArray());
				BufferedImage bufferedImage = ImageIO.read(in);
				Graphics graphics = bufferedImage.getGraphics();
				// String markerCode = new String("Marker Description 1");
				String markerCode = new String("Table 1");

				// String key = markers.getLayoutMarkerCode();
				// graphics.setColor(Color.LIGHT_GRAY);
				// graphics.fillRect(0, 0, 200, 50);
				/*
				 * graphics.setColor(Color.BLACK); graphics.setFont(new
				 * java.awt.Font("Arial Black", Font.NORMAL, 10)); String
				 * markerCode = new String("Table 1"); int xPos = 13
				 * -markerCode.length()/2; System.out.println("Xpos: "+xPos);
				 * graphics.drawString(markerCode.toUpperCase(), xPos, 144);
				 */

				/*
				 * java.awt.Font font = new java.awt.Font("Arial Black",
				 * Font.NORMAL, 10); FontMetrics fontMetrics =
				 * graphics.getFontMetrics(font);
				 * 
				 * /// draw title graphics.setColor(Color.BLACK);
				 * graphics.setFont(font); int titleLen =
				 * fontMetrics.stringWidth(markerCode); System.out.println(
				 * "Xpos: "+ ((144 / 2) - titleLen/2));
				 * graphics.drawString(markerCode, (144 / 2) - (titleLen/2),
				 * 144);
				 */

				boeOutput = new ByteArrayOutputStream();
				// ImageIO.write(bufferedImage, "png", boeOutput);
				// ImageIO.write(bufferedImage, "png", new
				// File("C://Vish//test22_new.png"));
				ImageIO.write(drawTextOnImage(markerCode, bufferedImage, 2), "png",
						new File("C://Vish//test22_new.png"));
				//// System.out.println("printing to " +
				//// file.getAbsolutePath());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		return boeOutput.toByteArray();
	}

	public static BufferedImage drawTextOnImage(String text, BufferedImage image, int space) {
		BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight() + space, BufferedImage.TRANSLUCENT);
		Graphics2D g2d = (Graphics2D) bi.createGraphics();
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g2d.addRenderingHints(
				new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));

		g2d.drawImage(image, 0, 0, null);

		g2d.setColor(Color.BLACK);
		g2d.setFont(new java.awt.Font("Arial Black", Font.NORMAL, 9));
		FontMetrics fm = g2d.getFontMetrics();
		int textWidth = fm.stringWidth(text.toUpperCase());

		// center text at bottom of image in the new space
		g2d.drawString(text.toUpperCase(), (bi.getWidth() / 2) - textWidth / 2, 144);

		g2d.dispose();
		return bi;
	}
	
	 /**
		 * Sets response for web service
		 * 
		 * @param result
		 * @param serviceIdentifier
		 */
		public static void setWebserviceResponse(Response<?> result, String status, String serviceIdentifier,
				String errorCode, String errorDesc) {
			result.setStatus(status);
			if (status.equalsIgnoreCase(Constants.ERROR)) {
				result.setErrorCode(errorCode);
			}
			result.setErrorDescription(errorDesc);
			result.setServiceIdentifier(serviceIdentifier);

		}

		/**
		 * Sets response for web service
		 * 
		 * @param result
		 * @param serviceIdentifier
		 */
		public static void setWebserviceResponse(Response<?> result, String status, String serviceIdentifier) {
			result.setStatus(status);
			result.setErrorDescription(status);
			result.setServiceIdentifier(serviceIdentifier);

		}

}
