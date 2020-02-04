package com.kyobeeUserService.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyobeeUserService.KyobeeUserServiceApplication;
import com.kyobeeUserService.dao.LanguageKeyMappingDAO;
import com.kyobeeUserService.dao.LookupDAO;
import com.kyobeeUserService.dao.OrganizationDAO;
import com.kyobeeUserService.dao.OrganizationTemplateDAO;
import com.kyobeeUserService.dao.UserDAO;
import com.kyobeeUserService.dto.CredentialsDTO;
import com.kyobeeUserService.dto.LanguageKeyMappingDTO;
import com.kyobeeUserService.dto.LanguageMasterDTO;
import com.kyobeeUserService.dto.LoginUserDTO;
import com.kyobeeUserService.dto.ResetPasswordDTO;
import com.kyobeeUserService.dto.SeatingMarketingPrefDTO;
import com.kyobeeUserService.dto.SmsTemplateDTO;
import com.kyobeeUserService.entity.LangMaster;
import com.kyobeeUserService.entity.Languagekeymapping;
import com.kyobeeUserService.entity.Lookup;
import com.kyobeeUserService.entity.Organization;
import com.kyobeeUserService.entity.OrganizationTemplate;
import com.kyobeeUserService.entity.User;
import com.kyobeeUserService.service.UserService;
import com.kyobeeUserService.util.CommonUtil;
import com.kyobeeUserService.util.EmailUtil;
import com.kyobeeUserService.util.LoggerUtil;
import com.kyobeeUserService.util.UserServiceConstants;
import com.kyobeeUserService.util.Exception.AccountNotActivatedExeception;
import com.kyobeeUserService.util.Exception.InvalidAuthCodeException;
import com.kyobeeUserService.util.Exception.InvalidLoginException;
import com.kyobeeUserService.util.Exception.UserNotFoundException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDAO userDAO;

	@Autowired
	EmailUtil emailUtil;

	@Autowired
	OrganizationDAO organizationDAO;

	@Autowired
	LookupDAO lookupDAO;

	@Autowired
	LanguageKeyMappingDAO languageKeyMappingDAO;

	@Autowired
	OrganizationTemplateDAO organizationTemplateDAO;

	//to validate user and fetch data needed after login in web and mobile, single API for login from web and mobile
	@Override
	public LoginUserDTO logInCredentialValidate(CredentialsDTO credentialsDTO)
			throws InvalidLoginException, AccountNotActivatedExeception {

		LoginUserDTO loginUserDTO;
		User user = userDAO.findByUserNameAndPassword(credentialsDTO.getUserName(),CommonUtil.encryptPassword(credentialsDTO.getPassword()));
		if (user != null) {
			if (user.getActive() ==  UserServiceConstants.ACTIVATEDUSER) {
				loginUserDTO = new LoginUserDTO();
				BeanUtils.copyProperties(user, loginUserDTO);
				//fetch organization details associated with user
				Organization organization = organizationDAO.fetchOrganizationByUserId(user.getUserID());
				if( (organization.getClientBase().equalsIgnoreCase(credentialsDTO.getClientBase()) && credentialsDTO.getDeviceType().equalsIgnoreCase(UserServiceConstants.WEBUSER) || (!credentialsDTO.getDeviceType().equalsIgnoreCase(UserServiceConstants.WEBUSER) ))) {
					BeanUtils.copyProperties(organization, loginUserDTO);
					loginUserDTO.setCompanyEmail(organization.getEmail());
					Map<String, String> defaultLanguageKeyMap = new HashMap<>();
					
					if(!credentialsDTO.getDeviceType().equalsIgnoreCase(UserServiceConstants.WEBUSER))
					{
						//fetch languages associated with org and arrange labels in key value 
						List<LanguageMasterDTO> languageList = languageKeyMappingDAO.fetchLanguageKeyMapForOrganization(organization.getOrganizationID());

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
							
							if (UserServiceConstants.ENGLISHLANGID == entry.getKey()) {
								defaultLanguageKeyMap = keymap;
							}

							languageKeyMappingDTO.setLanguageMap(keymap);
							langkeyMapList.add(languageKeyMappingDTO);
						}

						loginUserDTO.setLanguagePref(langkeyMapList);
					}

					//fetch seating pref and marketing pref associated with org
					List<Lookup> lookupList = lookupDAO.fetchSeatingAndMarketingPref(organization.getOrganizationID(), UserServiceConstants.SEATINGPREFID,UserServiceConstants.MARKETINGPREFID);
					List<SeatingMarketingPrefDTO> seatingPrefList = new ArrayList<>();
					List<SeatingMarketingPrefDTO> marketingPrefList = new ArrayList<>();

					SeatingMarketingPrefDTO seatingPref;
					SeatingMarketingPrefDTO marketingPref;
					//to separate seating pref and marketing pref 
					for (Lookup lookup : lookupList) {
						if (lookup.getLookuptype().getLookupTypeID() == UserServiceConstants.SEATINGPREFID) {
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
						} else if (lookup.getLookuptype().getLookupTypeID() == UserServiceConstants.MARKETINGPREFID) {
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

					List<OrganizationTemplate> templetList = organizationTemplateDAO
							.fetchSmsTemplateForOrganization(organization.getOrganizationID());
					List<SmsTemplateDTO> smsTemplateList = new ArrayList<>();
					SmsTemplateDTO smsTemplate;
					for (OrganizationTemplate template : templetList) {
						smsTemplate = new SmsTemplateDTO();
						BeanUtils.copyProperties(template, smsTemplate);
						smsTemplateList.add(smsTemplate);
					}
					loginUserDTO.setSmsTemplate(smsTemplateList);
					loginUserDTO.setSeatingpref(seatingPrefList);
					loginUserDTO.setMarketingPref(marketingPrefList);
					loginUserDTO.setFooterMsg(UserServiceConstants.FOOTERMSG);
					loginUserDTO.setLogoPath(UserServiceConstants.KYOBEESERVERURL+loginUserDTO.getOrganizationID()+UserServiceConstants.EXTENSION);
					return loginUserDTO;

				}
				else {
					throw new InvalidLoginException("Invalid username or password");
				}
			} else {
				throw new AccountNotActivatedExeception("Account is not activated.Please Activate your account using code given at registration.");
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
			Date today=new Date();
			if(today.after(user.getActivationExpiryDate())) {
				//if user enters authcode after 24 hour 
				throw new InvalidAuthCodeException("Invalid Authcode exception");
			}
			else {
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
		Date today=new Date();
		long hour = 3600*1000; 
		if (user != null) {
			String authcode;
			//if authcode is null then generate new one
			if (user.getAuthCode() == null || user.getAuthCode().equals("")) {
				
				Date nextDay=new Date(today.getTime() + 24 * hour);
				authcode = CommonUtil.generateRandomToken().toString();
				user.setAuthCode(authcode);
				user.setActivationExpiryDate(nextDay);
				userDAO.save(user);
			} else {
				
				if(today.after(user.getActivationExpiryDate())) {
					//24 hour are complete then generate new one
					authcode = CommonUtil.generateRandomToken().toString();
					user.setAuthCode(authcode);				
					Date nextDay=new Date(today.getTime() + 24 * hour);
					user.setActivationExpiryDate(nextDay);
					userDAO.save(user);
				}
				else {
					authcode = user.getAuthCode();
				}	
			}
			String forgotPasswordURL = UserServiceConstants.KYOBEEWEBHOST + UserServiceConstants.RESETPWDLINK + user.getUserID() + "/"
					+ authcode;

			LoggerUtil.logInfo("url:- "+forgotPasswordURL);
			StringBuilder htmlContent = new StringBuilder();

			htmlContent.append("<p>Hi " + user.getFirstName() + " " + user.getLastName() + ", </p>");
			htmlContent.append("<p>We received a request to reset your password for your Kyobee account: "
					+ user.getEmail() + ". We are here to help!");
			htmlContent.append("<p>Use the link below to set up a new password for your account.</p>");
			htmlContent.append("<p><a href='" + forgotPasswordURL + "'>Click here</a></p>");
			htmlContent.append("<p>If you did not request to reset your password, then simply ignore this mail.</p>");
			htmlContent.append("<p>We love hearing from you.</p>");
			htmlContent.append(
					"<p>Email us at " + UserServiceConstants.KYOBEEMAILID + " if you have any other questions! </p>");
			htmlContent.append("<p>Best,<br/>Kyobee</p>");
			response = "password sent successufully to your registered account";
			/*
			 * emailUtil.sendEmail(user.getEmail(), UserServiceConstants.KYOBEEMAILID,
			 * "Forgot Password Email", htmlContent.toString());
			 */
		} else {
			throw new UserNotFoundException("Please Enter valid email address.");
		}
		return response;
	}

}