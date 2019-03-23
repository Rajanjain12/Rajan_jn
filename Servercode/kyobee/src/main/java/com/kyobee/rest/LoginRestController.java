package com.kyobee.rest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kyobee.dao.impl.UserDAO;
import com.kyobee.dto.AddressDTO;
import com.kyobee.dto.GuestDTO;
import com.kyobee.dto.GuestMarketingPreference;
import com.kyobee.dto.GuestPreferencesDTO;
import com.kyobee.dto.LanguageMasterDTO;
import com.kyobee.dto.OrganizationTemplateDTO;
import com.kyobee.dto.ResetPasswordDTO;
import com.kyobee.dto.UserDTO;
import com.kyobee.dto.common.Credential;
import com.kyobee.dto.common.Response;
import com.kyobee.dto.ScreensaverDTO;
import com.kyobee.entity.Organization;
import com.kyobee.entity.User;
import com.kyobee.exception.NoSuchUsernameException;
import com.kyobee.exception.RsntException;
import com.kyobee.service.IOrganizationService;
import com.kyobee.service.ISecurityService;
import com.kyobee.service.IWaitListService;
import com.kyobee.util.SessionContextUtil;
import com.kyobee.util.common.CommonUtil;
import com.kyobee.util.common.Constants;
import com.kyobee.util.common.LoggerUtil;
import com.sun.net.httpserver.Authenticator.Success;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/rest")
public class LoginRestController {

	@Autowired
	ISecurityService securityService;
	
	@Autowired
	private IWaitListService waitListService;
	
	@Autowired
	IOrganizationService orgService;
	
	@Autowired
	SessionContextUtil sessionContextUtil;

	//Print success message from property file  by Aarshi(11/03/2019)
	 @Value("${Successful_verification}")
	 private String  Successful_verification;
			
    //Print Error message from property file  by Aarshi(11/03/2019)
    @Value("${Unsuccessful_verification}")
	private String Unsuccessful_verification;
			
    //Set Successful verification  mail message by Aarshi(12/03/2019)
    @Value("${ACTIVATION_MAIL_SUCCESS}")
    private String  ACTIVATION_MAIL_SUCCESS;
	
    //Set Unsuccessful verification  mail message by Aarshi(12/03/2019)
    @Value("${ACTIVATION_MAIL_FAIL}")
	private String ACTIVATION_MAIL_FAIL;
    
    @Value("${CHANGE_PASSWORD_SUCCESS}")
	private String CHANGE_PASSWORD_SUCCESS;
	
    
    @Value("${CHANGE_PASSWORD_FAIL}")
	private String CHANGE_PASSWORD_FAIL;
	
	/*
	 * This api is used only for browser and it is validated as per clientbase as of 2-Mar-2017
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public Response<UserDTO> login(@RequestBody Credential credenitals, HttpServletRequest request) {
		Response<UserDTO> response = new Response<UserDTO>();
		User loginUser = null;
		try {
			if (credenitals != null && credenitals.getUsername() != null  && credenitals.getPassword() != null && credenitals.getClientBase() != null) {
			
				loginUser = securityService.loginAndFetchUser(credenitals.getUsername(), credenitals.getPassword(),
						credenitals.getClientBase());

				if (loginUser != null) {

					if (!loginUser.isActive()) {
						response.setStatus("ERROR");
						CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "", "",
								"Account is active. Please contact customer support.");
						return response;
					}
					
					HttpSession sessionObj = request.getSession();
					loadOrgContextData(loginUser.getUserId());
					UserDTO userDTO = prepareUserObj(loginUser);
					//userDTO.setClientBase(credenitals.getClientBase());
					sessionObj.setAttribute(Constants.USER_OBJ, userDTO);
					response.setServiceResult(userDTO);
					LoggerUtil.logInfo("---- Login successful ----- : " + userDTO.getUserName());
					response.setStatus("SUCCESS");
					CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, "");
				} else {
					response.setStatus("ERROR");
					CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "", "",
							"Login Failed. Invalid Username or Password");
					return response;
				}
			} else {
				response.setStatus("ERROR");
				CommonUtil.setWebserviceResponse(response, Constants.ERROR, "");
			}

		} catch (RsntException e) {
			response.setStatus("ERROR");
			CommonUtil.setWebserviceResponse(response, Constants.ERROR, "");
			LoggerUtil.logError("Error while login", e);
		}
		return response;
	}
	//Pampaniya Shweta for Forgot Password....
	@RequestMapping(value = "/forgotPwd", method = RequestMethod.GET, produces = "application/json")
	   public Response<String> forgotPassword(@RequestParam String email) throws RsntException 
	   {
			Response<String> response = new Response<String>();
			try
			{	
				User user = securityService.forgotPassword(email);
				response.setServiceResult("Email has been sent to your registered email Id. Please follow the steps to reset your password");
				CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, "");
			} catch (RsntException e)
			{
				response.setServiceResult("Invalid Email. Please enter valid Email");
				CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "", "",
							"Error while sending forgot password email");
				LoggerUtil.logError("Error while sending forgot password email", e);
			}
			return response;
		}
	 
	  //Pampaniya Shweta for check url is correct or not...
	@RequestMapping(value = "/validateResetPwdurl", method = RequestMethod.GET, produces = "application/json")
	 public Response<String> validateResetPwdurl(@RequestParam Integer userId, @RequestParam String authcode) throws RsntException 
	{
	  Response<String> response = new Response<String>();
	  try {
		    String userAuthcode =securityService.getAuthCode(userId);
		    if(authcode.equals(userAuthcode))
		    {
		    	response.setServiceResult("URL validated successfully");
		    	CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, "");
		    }
		   else
		   {
			   response.setServiceResult("Invalid URL. Verification failure. Please check the url again or contact support.");
			   CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "", "",
						"Error Occur");
		   }
		   return response;
	  }catch (Exception e) 
	  {
		   throw new RsntException(e);
	  }
	}
	
	//Pampaniya Shweta for reset password...
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = "application/json")
	 public Response<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) throws RsntException 
	{
	  Response<String> response = new Response<String>();
	  try
	  {
		  	User user = securityService.resetPassword(resetPasswordDTO.getUserId(), resetPasswordDTO.getPassword());	
		  	response.setServiceResult("Password Reset SucessFully. Please login to continue.");
		  	CommonUtil.setWebserviceResponse(response,Constants.SUCCESS, "");
	  }catch(RsntException e)
	  {
		  response.setServiceResult("Error occured while resetting the password. Please contact support or try again later.");
		  CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "", "",
					"Error occured while resetting the passord. Please contact support or try again later.");
	  }
	   return response;
	 }
	/*
	 * This api is used by app(iOS/Android) clientbase doesnot affect here as of 2-Mar-2017
	 */
	@RequestMapping(value = "/loginCredAuth", method = RequestMethod.GET, produces = "application/json")
	public String loginCredAuth(@RequestParam String username, @RequestParam String password){
		final Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		try {
			List<Object[]>  result = securityService.loginCredAuth(username, password);
	   		Object[] loginDetail = result.get(0);
	   		
			if(loginDetail[0].toString().equals(CommonUtil.encryptPassword(password))){
				rootMap.put("OrgId",loginDetail[1].toString());
				rootMap.put("logofile name",loginDetail[2].toString());
				rootMap.put("clientBase",loginDetail[3].toString());
				if(loginDetail[4]!=null)
					rootMap.put("smsRoute", loginDetail[4].toString());
				else
					rootMap.put("smsRoute", loginDetail[4]);
				
				if(loginDetail[5]!=null){
					rootMap.put("MaxParty", loginDetail[5].toString());
				}else{
					rootMap.put("MaxParty", 0);
				}
				
				if(loginDetail[6]!=null){
					rootMap.put("defaultLangId", loginDetail[6].toString());
				}
				
				ScreensaverDTO screensaver = null;
				try {
					screensaver = waitListService.getOrganizationScreensaver(Long.valueOf(loginDetail[1].toString()).longValue());
					rootMap.put("screensaver", screensaver);
				} catch (Exception e) {
					e.printStackTrace();
					LoggerUtil.logError(e.getMessage(), e);
				}
				
				List<LanguageMasterDTO> langPref = null;
				try {
					langPref = waitListService.getOrganizationLanguagePref(Long.valueOf(loginDetail[1].toString()).longValue());
					rootMap.put("languagepref",langPref);
				} catch (Exception e) {
					LoggerUtil.logError(e.getMessage(), e);
				}
				
				List<OrganizationTemplateDTO> orgTemplates =  null;
				try {
					long langID=1;
					orgTemplates =	waitListService.getOrganizationTemplates(Long.valueOf(loginDetail[1].toString()).longValue(),langID,null);
					rootMap.put("smsTemplates",orgTemplates);
				}catch(Exception e){
					LoggerUtil.logError(e.getMessage(), e);
				}
				
				List<GuestPreferencesDTO> searPref =  null;
				try {
					searPref=	waitListService.getOrganizationSeatingPref(Long.valueOf(loginDetail[1].toString()).longValue());
					rootMap.put("success", "0");
					rootMap.put("seatpref",searPref);
				}catch(Exception e){
					LoggerUtil.logError(e.getMessage(), e);
				}
				// change by sunny for get Marketing Preference List (2018-07-06)
				List<GuestMarketingPreference> marketingPref =  null;
				try {
					marketingPref=	waitListService.getOrganizationMarketingPref(Long.valueOf(loginDetail[1].toString()).longValue());
					rootMap.put("success", "0");
					rootMap.put("marketingPref",marketingPref);
				}catch(Exception e){
					LoggerUtil.logError(e.getMessage(), e);
				}
				
			}else{
				rootMap.put("success", "-1");
				rootMap.put(Constants.RSNT_ERROR, "Invalid Username or Password.");

			}
		}catch(NoSuchUsernameException ne) {
			rootMap.put("success", "-1");
			rootMap.put(Constants.RSNT_ERROR, ne.getMessage());
		}catch(Exception e) {
			rootMap.put("success", "-1");
			rootMap.put(Constants.RSNT_ERROR, "Something wrong occurred");
			System.out.println(e.getMessage());
		}
		
		
		final JSONObject jsonObject = JSONObject.fromObject(rootMap);
		return jsonObject.toString();
	}
	
	@RequestMapping(value = "/userDetails", method = RequestMethod.GET, produces = "application/json")
	public Response<UserDTO> fetchUserDetails(HttpServletRequest request) {
		Response<UserDTO> userDetails = new Response<UserDTO>();
		HttpSession sessionObj = request.getSession();
		UserDTO userDTO = (UserDTO)sessionObj.getAttribute(Constants.USER_OBJ);
		if(!CommonUtil.isNullOrEmpty(userDTO)){
			userDetails.setServiceResult(userDTO);
			CommonUtil.setWebserviceResponse(userDetails, Constants.SUCCESS, "");
		} else {
			userDetails.setServiceResult(null);
			CommonUtil.setWebserviceResponse(userDetails, Constants.ERROR, null, null, null);
		}
		
		return userDetails;
	}
	
	private void loadOrgContextData(Long userId) throws RsntException {
		try {
			securityService.getUserOrganization(userId);
			// Long orgId = new Long(1);
		} catch (Exception e) {
			LoggerUtil.logError("Exception: ", e);
			throw e;
		}

	}
	
	private UserDTO prepareUserObj(User loginUser) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(loginUser.getUserId());
		userDTO.setUserName(loginUser.getUserName());
		userDTO.setFirstName(loginUser.getFirstName());
		userDTO.setLastName(loginUser.getLastName());
		userDTO.setEmail(loginUser.getEmail());
		userDTO.setPrimaryContactNo(loginUser.getPrimaryContactNo());
		userDTO.setAlternateContactNo(loginUser.getAlternateContactNo());
		userDTO.setActivationId(loginUser.getActivationId());
		userDTO.setAddress(loginUser.getAddress());
		userDTO.setActive(loginUser.isActive());
		userDTO.setPermissionList(new ArrayList<String>());
		userDTO.setOrganizationId((Long) sessionContextUtil.get(Constants.CONST_ORGID));
		Organization org=orgService.getOrganizationById((Long) sessionContextUtil.get(Constants.CONST_ORGID));
		userDTO.setSmsRoute(org.getSmsRoute());
		userDTO.setMaxParty(org.getMaxParty());
		userDTO.setDefaultLangId(org.getDefaultLangId());
		userDTO.setClientBase(org.getClientBase());
		final List<String> userPermissions = securityService.getUserPermissions(loginUser.getUserId());
		if (userPermissions != null && !userPermissions.isEmpty()) {
			for (final String permission : userPermissions) {
				userDTO.getPermissionList().add(permission);
			}
		}
		return userDTO;
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST, produces = "application/json")
	public Response<Boolean> signup(@RequestBody Credential credentials) throws Exception {
		Response<Boolean> userDetails = new Response<Boolean>();
		try {

			if (credentials.getCompanyName() != null && credentials.getCompanyPrimaryPhone() != null && 
					credentials.getCompanyEmail() != null 
					&& credentials.getFirstName() != null && credentials.getLastName() != null && credentials.getUsername() != null
					&& credentials.getPassword() != null && credentials.getConfirmPassword() != null) {
				
				if(securityService.isDuplicateUser(credentials.getUsername())){
					userDetails.setServiceResult(false);
					CommonUtil.setWebserviceResponse(userDetails, Constants.FAILURE, "", "",
							"Username/Email already exists. Please try a different one.");
					return userDetails;
				}
				
				if(securityService.isDuplicateOrganization(credentials.getCompanyName())){
					userDetails.setServiceResult(false);
					CommonUtil.setWebserviceResponse(userDetails, Constants.FAILURE, "", "",
							"Company name already exists. Please try a different one.");
					return userDetails;
				}
				
				credentials.setPassword(CommonUtil.encryptPassword(credentials.getPassword()));
				
				User signedUpUser = securityService.signupUser(credentials);
				
				if (signedUpUser != null) {
					userDetails.setServiceResult(true);
					CommonUtil.setWebserviceResponse(userDetails, Constants.SUCCESS, "");
				} else {
					userDetails.setServiceResult(false);
					CommonUtil.setWebserviceResponse(userDetails, Constants.FAILURE, "");
				}
			}
		} catch (Exception e) {
			LoggerUtil.logError("Error while login", e);
			userDetails.setServiceResult(false);
			CommonUtil.setWebserviceResponse(userDetails, Constants.FAILURE, "", "",
					"Error occured while sign up. Please contact support");
		}
		return userDetails;
	}

	//User Account Activation  by Aarshi(11/03/2019)


	@RequestMapping(value = "/activateUser", method = RequestMethod.POST, produces = "application/json")
	public Response<String> activateUser(@RequestParam String authCode,@RequestParam  Integer  userId)throws RsntException{
		
		Response<String> response = new Response<String>();
	   Boolean isVeried=securityService.authVerification(userId, authCode);
     	try
	    {
	    	if(isVeried.equals(true)){
	             response.setServiceResult(Successful_verification);
	             CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, "", "",
	            		 Successful_verification);
	        }else{
		        response.setServiceResult(Unsuccessful_verification);
		        CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "", "",
		        		Unsuccessful_verification);
			
	        }
	    }catch(Exception ex)
     	{
	    	LoggerUtil.logError("Error AuthCode Verification", ex);
			response.setServiceResult(Unsuccessful_verification);
			CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "", "",
					Unsuccessful_verification);
			throw new RsntException("Activing User"+userId, ex);
		
     	}
	   return response;
	   }

      //Send authentication mail to user by Aarshi(12/03/2019)
    	@RequestMapping(value = "/activationmail", method = RequestMethod.POST, produces = "application/json")
        public Response<String> activationmail(@RequestParam Integer userId)throws RsntException{
    		Response<String> response = new Response<String>();
			
    		try{
    			securityService.sendActivationMail(userId);
    			 response.setServiceResult(ACTIVATION_MAIL_SUCCESS);
    		     CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, "", "",
 	 					ACTIVATION_MAIL_SUCCESS);
 	        
    		}catch(Exception ex){
    			LoggerUtil.logError("Error Send account activtion mail", ex);
    			response.setServiceResult(ACTIVATION_MAIL_FAIL);
    			CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "", "",
    					ACTIVATION_MAIL_FAIL);
    			throw new RsntException("SendAuthCode Mail"+userId, ex);
    		}
    		return response;
	    }
    	
    	// Forgot Password By Aarshi Patel(13-03-2019)
    	@RequestMapping(value = "/forgotPwd/V2", method = RequestMethod.GET, produces = "application/json")
    	   public Response<String> forgotPasswordT(@RequestParam String email) throws RsntException 
    	   {
    			Response<String> response = new Response<String>();
    			try
    			{	
    				User user = securityService.forgotPassword(email);
    				response.setServiceResult("Email has been sent to your registered email Id. Please follow the steps to reset your password");
    				CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, "");
    			} catch (RsntException e)
    			{
    				response.setServiceResult("Invalid Email. Please enter valid Email");
    				CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "", "",
    							"Error while sending forgot password email");
    				LoggerUtil.logError("Error while sending forgot password email", e);
    			}
    			return response;
    		}
    	 
    

    	 //Change Password By Aarshi(13/03/2019)
    	@RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = "application/json")
        public Response<String> changePassword(@RequestParam Integer userId,@RequestParam String oldPassword,@RequestParam String newPassowrd)throws RsntException{
    		Response<String> response = new Response<String>();
    	try{
    			
    		Boolean checkStatus=securityService.changePassword(userId,oldPassword,newPassowrd);
    			
    		if(checkStatus.equals(true)){
    			response.setServiceResult(CHANGE_PASSWORD_SUCCESS);
				CommonUtil.setWebserviceResponse(response, Constants.SUCCESS, "","",CHANGE_PASSWORD_SUCCESS);
    		}else{
    			CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "", "",CHANGE_PASSWORD_FAIL);
			    LoggerUtil.logError("Password not match"+userId);
    		 }
    		}catch(Exception ex){
    			response.setServiceResult(CHANGE_PASSWORD_SUCCESS);
    			CommonUtil.setWebserviceResponse(response, Constants.FAILURE, "", "",CHANGE_PASSWORD_FAIL);
			LoggerUtil.logError("Error while change password"+userId, ex);
    		}
    		return response;
	    }	
    	
    	@RequestMapping(value = "/signup/V2", method = RequestMethod.POST, produces = "application/json")
    	public Response<Boolean> signupV2(@RequestBody Credential credentials) throws Exception {
    		Response<Boolean> userDetails = new Response<Boolean>();
    		try {

    			if (credentials.getCompanyName() != null && credentials.getCompanyPrimaryPhone() != null && 
    					credentials.getCompanyEmail() != null 
    					&& credentials.getFirstName() != null && credentials.getLastName() != null && credentials.getUsername() != null
    					&& credentials.getPassword() != null && credentials.getConfirmPassword() != null) {
    				
    				if(securityService.isDuplicateUser(credentials.getUsername())){
    					userDetails.setServiceResult(false);
    					CommonUtil.setWebserviceResponse(userDetails, Constants.FAILURE, "", "",
    							"Username/Email already exists. Please try a different one.");
    					return userDetails;
    				}
    				
    				if(securityService.isDuplicateOrganization(credentials.getCompanyName())){
    					userDetails.setServiceResult(false);
    					CommonUtil.setWebserviceResponse(userDetails, Constants.FAILURE, "", "",
    							"Company name already exists. Please try a different one.");
    					return userDetails;
    				}
    				
    				credentials.setPassword(CommonUtil.encryptPassword(credentials.getPassword()));
    				
    				User signedUpUser = securityService.signupUser(credentials);
    				
    				if (signedUpUser != null) {
    					userDetails.setServiceResult(true);
    					CommonUtil.setWebserviceResponse(userDetails, Constants.SUCCESS, "");
    				} else {
    					userDetails.setServiceResult(false);
    					CommonUtil.setWebserviceResponse(userDetails, Constants.FAILURE, "");
    				}
    			}
    		} catch (Exception e) {
    			LoggerUtil.logError("Error while login", e);
    			userDetails.setServiceResult(false);
    			CommonUtil.setWebserviceResponse(userDetails, Constants.FAILURE, "", "",
    					"Error occured while sign up. Please contact support");
    		}
    		return userDetails;
    	}
    	
   
    	
}
