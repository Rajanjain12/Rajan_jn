package com.kyobeeUserService.serviceImpl;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.kyobeeUserService.dao.CountryDAO;
import com.kyobeeUserService.dao.CustomerDAO;
import com.kyobeeUserService.dao.LanguageKeyMappingDAO;
import com.kyobeeUserService.dao.LookupDAO;
import com.kyobeeUserService.dao.OrganizationDAO;
import com.kyobeeUserService.dao.OrganizationTemplateDAO;
import com.kyobeeUserService.dao.OrganizationTypeDAO;
import com.kyobeeUserService.dao.RoleDAO;
import com.kyobeeUserService.dao.SmsTemplateLanguageMappingDAO;
import com.kyobeeUserService.dao.UserDAO;
import com.kyobeeUserService.dto.AddressDTO;
import com.kyobeeUserService.dto.CountryDTO;
import com.kyobeeUserService.dto.CredentialsDTO;
import com.kyobeeUserService.dto.LanguageKeyMappingDTO;
import com.kyobeeUserService.dto.LanguageMasterDTO;
import com.kyobeeUserService.dto.LoginUserDTO;
import com.kyobeeUserService.dto.OrganizationDTO;
import com.kyobeeUserService.dto.PlaceDTO;
import com.kyobeeUserService.dto.ResetPasswordDTO;
import com.kyobeeUserService.dto.SeatingMarketingPrefDTO;
import com.kyobeeUserService.dto.SignUpDTO;
import com.kyobeeUserService.dto.SmsTemplateDTO;
import com.kyobeeUserService.dto.UserSignUpDTO;
import com.kyobeeUserService.entity.Customer;
import com.kyobeeUserService.entity.Lookup;
import com.kyobeeUserService.entity.Organization;
import com.kyobeeUserService.entity.OrganizationCategory;
import com.kyobeeUserService.entity.OrganizationTemplate;
import com.kyobeeUserService.entity.OrganizationType;
import com.kyobeeUserService.entity.OrganizationUser;
import com.kyobeeUserService.entity.Role;
import com.kyobeeUserService.entity.SmsTemplateLanguageMapping;
import com.kyobeeUserService.entity.User;
import com.kyobeeUserService.service.UserService;
import com.kyobeeUserService.util.CommonUtil;
import com.kyobeeUserService.util.EmailUtil;
import com.kyobeeUserService.util.LoggerUtil;
import com.kyobeeUserService.util.UserServiceConstants;
import com.kyobeeUserService.util.Exception.AccountNotActivatedExeception;
import com.kyobeeUserService.util.Exception.DuplicateEmailExeception;
import com.kyobeeUserService.util.Exception.DuplicateUserNameExeception;
import com.kyobeeUserService.util.Exception.InvalidAuthCodeException;
import com.kyobeeUserService.util.Exception.InvalidActivationCodeException;
import com.kyobeeUserService.util.Exception.InvalidLoginException;
import com.kyobeeUserService.util.Exception.InvalidZipCodeException;
import com.kyobeeUserService.util.Exception.UserNotFoundException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private OrganizationDAO organizationDAO;

	@Autowired
	private LookupDAO lookupDAO;

	@Autowired
	private LanguageKeyMappingDAO languageKeyMappingDAO;

	@Autowired
	private OrganizationTemplateDAO organizationTemplateDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private OrganizationTypeDAO organizationTypeDAO;

	@Autowired
	private SmsTemplateLanguageMappingDAO smsTemplateLanguageMappingDAO;

	@Autowired
	private WebClient.Builder webClientBuilder;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private EmailUtil emailUtil;

	@Autowired
	private CountryDAO countryDAO;
	
	@Autowired
	private CustomerDAO customerDAO;

	// to validate user and fetch data needed after login in web and mobile, single
	// API for login from web and mobile
	@Override
	public LoginUserDTO logInCredentialValidate(CredentialsDTO credentialsDTO)
			throws InvalidLoginException, AccountNotActivatedExeception {

		LoginUserDTO loginUserDTO;
		String salt = userDAO.getSaltString(credentialsDTO.getUserName());
		User user = userDAO.findByUserNameAndPassword(credentialsDTO.getUserName(),
				CommonUtil.encryptPassword(credentialsDTO.getPassword()+salt));
		if (user != null) {
			if (user.getActive() == UserServiceConstants.ACTIVATED_USER) {
				loginUserDTO = new LoginUserDTO();
				BeanUtils.copyProperties(user, loginUserDTO);
				// fetch organization details associated with user
				Organization organization = organizationDAO.fetchOrganizationByUserId(user.getUserID());
				if ((organization.getClientBase().equalsIgnoreCase(credentialsDTO.getClientBase())
						&& credentialsDTO.getDeviceType().equalsIgnoreCase(UserServiceConstants.WEB_USER)
						|| (!credentialsDTO.getDeviceType().equalsIgnoreCase(UserServiceConstants.WEB_USER)))) {
					BeanUtils.copyProperties(organization, loginUserDTO);
					loginUserDTO.setCompanyEmail(organization.getEmail());
					Map<String, String> defaultLanguageKeyMap = new HashMap<>();

					// fetch languages associated with org and arrange labels in key value
					List<LanguageMasterDTO> languageList = languageKeyMappingDAO
							.fetchLanguageKeyMapForOrganization(organization.getOrganizationID());
					List<LanguageKeyMappingDTO> langkeyMapList = new ArrayList<>();
					LanguageKeyMappingDTO languageKeyMappingDTO;

					Map<Integer, List<LanguageMasterDTO>> langListById = languageList.stream()
							.collect(Collectors.groupingBy(LanguageMasterDTO::getLangId));

					for (Map.Entry<Integer, List<LanguageMasterDTO>> entry : langListById.entrySet()) {

						LanguageMasterDTO first = entry.getValue().get(0);
						languageKeyMappingDTO = new LanguageKeyMappingDTO();
						languageKeyMappingDTO.setLangId(entry.getKey());
						languageKeyMappingDTO.setLangName(first.getLangName());
						languageKeyMappingDTO.setLangIsoCode(first.getLangIsoCode());
						Map<String, String> keymap = new HashMap<>();
						keymap.put(first.getKeyName(), first.getValue());

						for (LanguageMasterDTO languageMasterDTO : entry.getValue()) {
							keymap.put(languageMasterDTO.getKeyName(), languageMasterDTO.getValue());
						}

						if (UserServiceConstants.ENGLISH_LANG_ID == entry.getKey()) {
							defaultLanguageKeyMap = keymap;
						}

						languageKeyMappingDTO.setLanguageMap(keymap);
						langkeyMapList.add(languageKeyMappingDTO);
					}

					loginUserDTO.setLanguagePref(langkeyMapList);

					// fetch seating pref and marketing pref associated with org
					List<Lookup> lookupList = lookupDAO.fetchOrgSeatingAndMarketingPref(
							organization.getOrganizationID(), UserServiceConstants.SEATING_PREF_ID,
							UserServiceConstants.MARKETING_PREF_ID);
					List<SeatingMarketingPrefDTO> seatingPrefList = new ArrayList<>();
					List<SeatingMarketingPrefDTO> marketingPrefList = new ArrayList<>();

					SeatingMarketingPrefDTO seatingPref;
					SeatingMarketingPrefDTO marketingPref;
					// to separate seating pref and marketing pref
					for (Lookup lookup : lookupList) {
						if (lookup.getLookuptype().getLookupTypeID() == UserServiceConstants.SEATING_PREF_ID) {
							seatingPref = new SeatingMarketingPrefDTO();
							seatingPref.setPrefValue(lookup.getName());
							seatingPref.setPrefValueId(lookup.getLookupID());
							if (defaultLanguageKeyMap.containsValue(lookup.getName())) {
								String key = null;
								for (Map.Entry<String, String> entry : defaultLanguageKeyMap.entrySet()) {
									if ((lookup.getName()).equals(entry.getValue())) {
										key = entry.getKey();
										seatingPref.setPrefKey(key);
										break;
									}
								}
							}
							seatingPrefList.add(seatingPref);
						} else if (lookup.getLookuptype().getLookupTypeID() == UserServiceConstants.MARKETING_PREF_ID) {
							marketingPref = new SeatingMarketingPrefDTO();
							marketingPref.setPrefValue(lookup.getName());
							marketingPref.setPrefValueId(lookup.getLookupID());
							if (defaultLanguageKeyMap.containsValue(lookup.getName())) {

								String key = null;
								for (Map.Entry<String, String> entry : defaultLanguageKeyMap.entrySet()) {
									if ((lookup.getName()).equals(entry.getValue())) {
										key = entry.getKey();
										marketingPref.setPrefKey(key);
										break;
									}
								}

							}
							marketingPrefList.add(marketingPref);
						}

					}
					// seating marketing pref end

					List<OrganizationTemplate> templetList = organizationTemplateDAO
							.fetchSmsTemplateForOrganization(organization.getOrganizationID());
					List<SmsTemplateDTO> smsTemplateList = new ArrayList<>();
					SmsTemplateDTO smsTemplate;
					for (OrganizationTemplate template : templetList) {
						smsTemplate = new SmsTemplateDTO();
						BeanUtils.copyProperties(template, smsTemplate);
						switch (template.getLevel()) {
						case 1:
							smsTemplate.setLevelName(UserServiceConstants.SMS_LEVEL_1_NAME);
							break;
						case 2:
							smsTemplate.setLevelName(UserServiceConstants.SMS_LEVEL_2_NAME);
							break;
						case 3:
							smsTemplate.setLevelName(UserServiceConstants.SMS_LEVEL_3_NAME);
							break;
						default:
							break;
						}
						smsTemplateList.add(smsTemplate);
					}
					loginUserDTO.setSmsTemplate(smsTemplateList);
					loginUserDTO.setSeatingpref(seatingPrefList);
					loginUserDTO.setMarketingPref(marketingPrefList);
					loginUserDTO.setFooterMsg(UserServiceConstants.FOOTER_MSG);
					loginUserDTO.setLogoPath(UserServiceConstants.KYOBEE_SERVER_URL + loginUserDTO.getOrganizationID()
							+ UserServiceConstants.EXTENSION);
					loginUserDTO.setNotifyFirst(organization.getNotifyUserCount());
					loginUserDTO.setPplBifurcation(organization.getPplBifurcation());

					return loginUserDTO;

				} else {
					throw new InvalidLoginException("Invalid username or password");
				}
			} else {
				throw new AccountNotActivatedExeception(
						"Account is not activated.Please Activate your account using code given at registration.");
			}
		} else {
			throw new InvalidLoginException("Invalid username or password");

		}

	}

	@Override
	public String resetPassword(ResetPasswordDTO resetpassword) throws InvalidAuthCodeException {
		String response;
		User user = userDAO.findByUserIDAndAuthCode(resetpassword.getUserId(), resetpassword.getAuthcode());
		if (user != null) {
			Date today = new Date();
			if (today.after(user.getActivationExpiryDate())) {
				// if user enters authcode after 24 hour
				throw new InvalidAuthCodeException("Invalid Authcode exception");
			} else {
				user.setPassword(CommonUtil.encryptPassword(resetpassword.getPassword()));
				userDAO.save(user);
				response = "password reset successfully.";
			}

		} else {
			throw new InvalidAuthCodeException("Invalid Authcode exception");
		}
		return response;
	}

	@Override
	public String forgotPassword(String username) throws UserNotFoundException {
		String response;
		User user = userDAO.findByUserName(username);

		if (user != null) {
			String authcode;
			Date today = new Date();
			// if authcode is null then generate new one
			if (user.getAuthCode() == null || user.getAuthCode().equals("")) {

				Date nextDay = CommonUtil.getDateByHour(24);
				authcode = CommonUtil.generateRandomToken().toString();
				user.setAuthCode(authcode);
				user.setActivationExpiryDate(nextDay);
				userDAO.save(user);
			} else {

				if (today.after(user.getActivationExpiryDate())) {
					// 24 hour are complete then generate new one
					authcode = CommonUtil.generateRandomToken().toString();
					user.setAuthCode(authcode);
					Date nextDay = CommonUtil.getDateByHour(24);
					user.setActivationExpiryDate(nextDay);
					userDAO.save(user);
				} else {
					authcode = user.getAuthCode();
				}
			}
			String forgotPasswordURL = UserServiceConstants.KYOBEE_WEB_HOST + UserServiceConstants.RESET_PWD_LINK
					+ user.getUserID() + "/" + authcode;

			LoggerUtil.logInfo("url:- " + forgotPasswordURL);

			VelocityEngine ve = emailUtil.velocityTemplate();

			Template template = ve.getTemplate("templates/ForgotPassword.vm");
			String name = user.getFirstName() + " " + user.getLastName();

			VelocityContext context = new VelocityContext();
			context.put(UserServiceConstants.NAME, name);
			context.put(UserServiceConstants.LINK, forgotPasswordURL);
			context.put(UserServiceConstants.EMAIL, user.getEmail());
			context.put(UserServiceConstants.KYOBEE_EMAIL, UserServiceConstants.KYOBEE_MAIL_ID);

			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			// ----
			response = "password sent successufully to your registered account";

			// Web-Client Builder call
			emailUtil.sendEmail(user.getEmail(), UserServiceConstants.EMAIL_SUBJECT, writer.toString());
			LoggerUtil.logInfo("Entering Util service");

		} else {
			throw new UserNotFoundException("Please Enter valid email address.");
		}
		return response;
	}

	@Override
	public void signUp(SignUpDTO signUpDTO) {

		// check if user exists or not

		Boolean exists = checkIfUserExist(signUpDTO.getEmail());

		if (!exists) {

			User user = new User();
			Date nextDay = CommonUtil.getDateByHour(24);
			BeanUtils.copyProperties(signUpDTO, user);
			user.setPassword(CommonUtil.encryptPassword(signUpDTO.getPassword()));
			user.setContactNoOne(signUpDTO.getContactNo());
			user.setActivationCode(CommonUtil.generateRandomToken().toString());
			user.setActivationExpiryDate(nextDay);
			user.setActive(UserServiceConstants.INACTIVE_USER);
			user.setCreatedAt(new Date());
			user.setCreatedBy(signUpDTO.getEmail());

			Role role = roleDAO.fetchRole(UserServiceConstants.DEFAULT_ROLE);

			/*
			 * Userrole userRole = new Userrole(); userRole.setRole(role);
			 * userRole.setCreatedBy(signUpDTO.getEmail()); userRole.setCreatedAt(new
			 * Date()); userRole.setUser(user);
			 * 
			 * List<Userrole> userRoles = new ArrayList<>(); userRoles.add(userRole);
			 * user.setUserroles(userRoles);
			 * 
			 * LoggerUtil.logInfo("user role inserted");
			 */

			OrganizationType organizationType = organizationTypeDAO
					.fetchOrganizationType(UserServiceConstants.DEFAULT_ORG_TYPE);

			Organization organization = new Organization();
			organization.setOrganizationName(signUpDTO.getStoreName());
			organization.setEmail(signUpDTO.getEmail());
			organization.setPrimaryPhone(new BigInteger(signUpDTO.getContactNo()));
			organization.setActive(UserServiceConstants.ACTIVE_ORG);
			organization.setOrganizationType(organizationType);
			organization.setCreatedBy(signUpDTO.getEmail());
			organization.setCreatedAt(new Date());
			organization.setSmsSignature(organization.getOrganizationName());
			organization.setOrganizationType(organizationType);
			LoggerUtil.logInfo("organization type inserted");
			List<OrganizationCategory> orgCategoryList = new ArrayList<>();
			List<Lookup> lookupList = lookupDAO.fetchSeatingAndMarketingPref(UserServiceConstants.SEATING_PREF_ID,
					UserServiceConstants.MARKETING_PREF_ID);

			OrganizationCategory orgCategory;
			for (Lookup lookup : lookupList) {
				orgCategory = new OrganizationCategory();
				orgCategory.setOrganization(organization);
				orgCategory.setLookup(lookup);
				orgCategory.setLookuptype(lookup.getLookuptype());
				orgCategoryList.add(orgCategory);
			}
			organization.setOrganizationcategories(orgCategoryList);

			LoggerUtil.logInfo("organization categoery inserted");

			/*
			 * Plan plan = planDAO.fetchPlan(signUpDTO.getPlanId());
			 * LoggerUtil.logInfo("planid:" + plan.getPlanId() + " " + plan.getPlanName());
			 */
			// OrganizationPlanSubscription organizationPlanSubscription = new
			// OrganizationPlanSubscription();
			/*
			 * organizationPlanSubscription.setOrganization(organization);
			 * organizationPlanSubscription.setPlan(plan);
			 * organizationPlanSubscription.setCreatedBy(signUpDTO.getEmail());
			 * organizationPlanSubscription.setCreatedAt(new Date());
			 */
			// temporary added code
			/*
			 * organizationPlanSubscription.setAmountPerUnit(new BigDecimal(0.0));
			 * organizationPlanSubscription.setCostPerAd(new BigDecimal(0.0));
			 * organizationPlanSubscription.setCreatedBy(signUpDTO.getEmail());
			 * organizationPlanSubscription.setCurrencyId(11); Calendar cal =
			 * Calendar.getInstance(); cal.setTime(new Date()); cal.add(Calendar.MONTH, 1);
			 * organizationPlanSubscription.setEndDate(cal.getTime());
			 * organizationPlanSubscription.setModifiedBy(signUpDTO.getEmail());
			 * organizationPlanSubscription.setModifiedAt(new Date());
			 * organizationPlanSubscription.setNoOfAdsPerUnit(0);
			 * organizationPlanSubscription.setNumberOfUnits(0);s
			 * organizationPlanSubscription.setOrganization(organization);
			 * organizationPlanSubscription.setStartDate(new Date());
			 * organizationPlanSubscription.setTerminateDate(null);
			 * organizationPlanSubscription.setTotalAmount(new BigDecimal(0.0));
			 * organizationPlanSubscription.setUnitId(9);
			 */
			// ---

			/*
			 * List<OrganizationPlanSubscription> orgPlanSubscriptionList = new
			 * ArrayList<>(); orgPlanSubscriptionList.add(organizationPlanSubscription);
			 */
			// organization.setOrganizationPlanSubscriptionList(orgPlanSubscriptionList);
			// LoggerUtil.logInfo("organization plan inserted");

			LoggerUtil.logInfo("going to fetch template");
			List<SmsTemplateLanguageMapping> smstemplateList = smsTemplateLanguageMappingDAO
					.fetchSmsTemplate(UserServiceConstants.DEFAULT_LANG);
			List<OrganizationTemplate> orgTemplateList = new ArrayList<>();
			LoggerUtil.logInfo("completed");
			OrganizationTemplate orgTemplate;
			for (SmsTemplateLanguageMapping smstemplate : smstemplateList) {
				orgTemplate = new OrganizationTemplate();
				BeanUtils.copyProperties(smstemplate, orgTemplate);
				orgTemplate.setLanguageID(smstemplate.getLangmaster().getLangID());
				orgTemplate.setOrganization(organization);
				orgTemplate.setCreatedAt(new Date());
				orgTemplateList.add(orgTemplate);
			}

			organization.setOrganizationtemplates(orgTemplateList);

			OrganizationUser organizationUser = new OrganizationUser();
			organizationUser.setOrganization(organization);
			organizationUser.setCreatedBy(signUpDTO.getEmail());
			organizationUser.setCreatedAt(new Date());
			organizationUser.setUser(user);
			LoggerUtil.logInfo("organization user inserted");

			List<OrganizationUser> organizationUserList = new ArrayList<>();
			organizationUserList.add(organizationUser);
			organization.setOrganizationusers(organizationUserList);
			user.setOrganizationusers(organizationUserList);

			User savedUser = userDAO.save(user); // sending activation mail
			sendActivationEmail(savedUser);

		} else {
			LoggerUtil.logInfo("user exists");
		}

	}

	@Override
	public Boolean checkIfUserExist(String email) {

		User user = userDAO.findByEmail(email);

		if (user != null) {
			return true;
		} else {
			return false;
		}
	}

	public void sendActivationEmail(User user) {

		VelocityEngine ve = emailUtil.velocityTemplate();
		Template tempalte = ve.getTemplate("templates/ActivationMail.vm");
		String name = user.getFirstName() + " " + user.getLastName();

		VelocityContext context = new VelocityContext();
		context.put(UserServiceConstants.NAME, name);
		context.put(UserServiceConstants.ACTIVATION_CODE, user.getActivationCode());

		StringWriter writer = new StringWriter();
		tempalte.merge(context, writer);

		// Web-Client Builder call
		emailUtil.sendEmail(user.getEmail(), UserServiceConstants.ACTIVATION_EMAIL, writer.toString());
		LoggerUtil.logInfo("Entering Util service");

	}

	@Override
	public String activateUser(String activationCode, Integer userId) throws InvalidActivationCodeException {

		String response = null;
		User user = userDAO.findByUserIDAndActivationCode(userId, activationCode);
		LoggerUtil.logInfo("user:" + user);
		if (user != null) {
			Date today = new Date();
			if (today.after(user.getActivationExpiryDate())) {
				// if user enters activation code after 24 hour
				throw new InvalidActivationCodeException("Invalid activation code exception");

			} else {
				user.setActive(UserServiceConstants.ACTIVATED_USER);
				user.setActivationCode(null);
				user.setActivationExpiryDate(null);
				userDAO.save(user);
				sendWelcomeEmail(user);

				response = "User account activated successfully.";
			}

		} else {
			throw new InvalidActivationCodeException("Invalid activation code exception");
		}
		return response;
	}

	public void sendWelcomeEmail(User user) {

		VelocityEngine ve = emailUtil.velocityTemplate();

		Template template = ve.getTemplate("templates/WelcomeMail.vm");
		String name = user.getFirstName() + " " + user.getLastName();

		VelocityContext context = new VelocityContext();
		context.put(UserServiceConstants.NAME, name);

		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		// Web-Client Builder call
		emailUtil.sendEmail(user.getEmail(), UserServiceConstants.WELCOME_EMAIL, writer.toString());
		LoggerUtil.logInfo("Entering Util service");

	}

	@Override
	public String resendCode(Integer userId) {

		Date today = new Date();
		Date nextDay = CommonUtil.getDateByHour(24);

		User user = userDAO.findByUserID(userId);

		if (user.getActive().equals(UserServiceConstants.ACTIVATED_USER)) {
			return "Account is already activated";
		} else {
			if (today.after(user.getActivationExpiryDate())) {
				user.setActivationCode(CommonUtil.generateRandomToken().toString());
				user.setActivationExpiryDate(nextDay);
				User savedUser = userDAO.save(user);
				// sending activation mail
				sendActivationEmail(savedUser);
			} else {
				sendActivationEmail(user);
			}
		}
		return "Code Resend Successfully";
	}

	@Override
	public PlaceDTO fetchLatLon(Integer zipCode) throws JSONException, InvalidZipCodeException {

		StringBuilder url = new StringBuilder();
		url.append(UserServiceConstants.GEOCODING_API);
		url.append(UserServiceConstants.GEOCODE_PARAM + zipCode);

		// call to google API
		String response = restTemplate.getForObject(url.toString(), String.class);

		JSONObject obj = new JSONObject(response);
		JSONArray loc = obj.getJSONArray("results");

		if (loc.length() != 0) {
			PlaceDTO placeDTO = new PlaceDTO();
			placeDTO.setLatitude(
					loc.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat"));
			placeDTO.setLongitude(
					loc.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng"));

			return placeDTO;
		} else {
			throw new InvalidZipCodeException("Invalid ZipCode");

		}

	}

	@Override
	public List<PlaceDTO> fetchPlaceList(String place, String latLon, String countryCode) throws JSONException {

		List<PlaceDTO> placeList = new ArrayList<>();

		StringBuilder url = new StringBuilder();
		url.append(UserServiceConstants.AUTOCOMPLETE_API);
		url.append(UserServiceConstants.PLACE_PARAM + place + UserServiceConstants.LOCATION_PARAM + latLon
				+ UserServiceConstants.COUNTRY_PARAM + countryCode);

		// call to google API
		String response = restTemplate.getForObject(url.toString(), String.class);

		JSONObject obj = new JSONObject(response);
		JSONArray loc = obj.getJSONArray("predictions");
		PlaceDTO placeDTO = null;
		JSONObject placelist;

		for (int i = 0; i < loc.length(); i++) {
			placeDTO = new PlaceDTO();
			placelist = loc.getJSONObject(i);
			placeDTO.setPlaceId(placelist.getString("place_id"));
			placeDTO.setPlaceName(placelist.getString("description"));
			placeList.add(placeDTO);
		}
		return placeList;

	}

	@Override
	public OrganizationDTO fetchPlaceDetails(String placeId) throws JSONException {

		OrganizationDTO orgDTO = new OrganizationDTO();
		AddressDTO addressDTO = new AddressDTO();

		StringBuilder url = new StringBuilder();
		url.append(UserServiceConstants.PLACE_DETAILS_API);
		url.append(UserServiceConstants.PLACE_DETAILS_PARAM + placeId);

		// call to google API
		String response = restTemplate.getForObject(url.toString(), String.class);

		JSONObject obj = new JSONObject(response);
		JSONObject loc = obj.getJSONObject("result");

		orgDTO.setOrganizationName(loc.getString("name"));
		orgDTO.setPrimaryPhone(new BigInteger(loc.getString("formatted_phone_number").replaceAll("[^0-9]", "")));
		addressDTO.setAddressLineOne(loc.getString("vicinity"));

		JSONArray array = loc.getJSONArray("address_components");
		JSONObject address = null;

		for (int i = 0; i < array.length(); i++) {
			address = array.getJSONObject(i);
			String list = address.getString("types");
			if (list.contains("locality")) {
				addressDTO.setCity(address.getString("long_name"));
			} else if (list.contains("administrative_area_level_1")) {
				addressDTO.setState(address.getString("short_name"));
			}
		}
		orgDTO.setAddressDTO(addressDTO);
		return orgDTO;

	}

	@Override
	public List<CountryDTO> fetchCountryList() {

		return countryDAO.fetchCountryList();
	}
	
	@Override
	public Integer addUser(UserSignUpDTO userSignUpDTO) throws DuplicateUserNameExeception, DuplicateEmailExeception {

		User savedUser = null;
		// check if user email exists or not
		Boolean exists = checkIfUserExist(userSignUpDTO.getEmail());

		if (!exists) {

			// check if username exists or not
			exists = checkIfUserNameExist(userSignUpDTO.getUserName());

			if (!exists) {

				User user = new User();
				Date nextDay = CommonUtil.getDateByHour(24);

				String salt = CommonUtil.getSaltString();

				BeanUtils.copyProperties(userSignUpDTO, user);
				user.setPassword(CommonUtil.encryptPassword(userSignUpDTO.getPassword() + salt));			
				user.setActive(UserServiceConstants.INACTIVE_USER);
				user.setCreatedAt(new Date());
				user.setCreatedBy(userSignUpDTO.getEmail());
				user.setSaltString(salt);
				user.setActivationCode(CommonUtil.generateRandomToken().toString());
				user.setActivationExpiryDate(nextDay);

				Role role = roleDAO.fetchRole(UserServiceConstants.CUSTOMER_ADMIN_ROLE);
				Customer customer = customerDAO.getOne(userSignUpDTO.getCustomerId());
				Organization organization = organizationDAO.getOne(userSignUpDTO.getOrgId());

				organization.setEmail(userSignUpDTO.getEmail());
				organization.setCreatedBy(userSignUpDTO.getEmail());
				
				OrganizationUser organizationUser = new OrganizationUser();
				organizationUser.setActive(UserServiceConstants.ACTIVATED_USER);
				organizationUser.setCreatedBy(userSignUpDTO.getEmail());
				organizationUser.setCreatedAt(new Date());
				organizationUser.setUser(user);
				organizationUser.setRole(role);
				organizationUser.setOrganization(organization);
				organizationUser.setCustomer(customer);			
				LoggerUtil.logInfo("organization user inserted");

				List<OrganizationUser> organizationUserList = new ArrayList<>();
				organizationUserList.add(organizationUser);
				user.setOrganizationusers(organizationUserList);

				savedUser = userDAO.save(user);	
				// sending activation mail
				sendActivationEmail(savedUser);

			} else {
				LoggerUtil.logInfo("userName exists");
				throw new DuplicateUserNameExeception("Username already exists. Please try different one.");
			}

		} else {
			LoggerUtil.logInfo("user email exists");
			throw new DuplicateEmailExeception("Email already exists. Please try different one.");
		}
		return savedUser.getUserID();
	}

	@Override
	public Boolean checkIfUserNameExist(String userName) {

		User user = userDAO.findByUserName(userName);

		if (user != null) {
			return true;
		} else {
			return false;
		}
	}


}