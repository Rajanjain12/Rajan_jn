package com.kyobeeUserService.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeUserService.dto.ResponseDTO;
import com.kyobeeUserService.dto.SignUpDTO;
import com.kyobeeUserService.dto.UserSignUpDTO;
import com.kyobeeUserService.service.UserService;
import com.kyobeeUserService.util.LoggerUtil;
import com.kyobeeUserService.util.UserServiceConstants;
import com.kyobeeUserService.util.Exception.AccountNotActivatedExeception;
import com.kyobeeUserService.util.Exception.DuplicateEmailExeception;
import com.kyobeeUserService.util.Exception.DuplicateUserNameExeception;
import com.kyobeeUserService.util.Exception.InvalidAuthCodeException;
import com.kyobeeUserService.util.Exception.InvalidLoginException;
import com.kyobeeUserService.util.Exception.InvalidPwdUrlException;
import com.kyobeeUserService.util.Exception.InvalidZipCodeException;
import com.kyobeeUserService.util.Exception.PasswordNotMatchException;
import com.kyobeeUserService.util.Exception.UserNotFoundException;
import com.kyobeeUserService.dto.CountryDTO;
import com.kyobeeUserService.dto.CredentialsDTO;
import com.kyobeeUserService.dto.LoginUserDTO;
import com.kyobeeUserService.dto.OrganizationDTO;
import com.kyobeeUserService.dto.PlaceDTO;
import com.kyobeeUserService.dto.ResetPasswordDTO;

@CrossOrigin
@RestController
@RequestMapping("/rest/user")
public class UserController {

	@Autowired
	private UserService userService;

	// For login of mobile and web both
	@PostMapping(value = "/login", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO login(@RequestBody CredentialsDTO credentialsDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			LoggerUtil.logInfo("inside login v1 v2");
			LoginUserDTO loginUserDTO = userService.logInCredentialValidate(credentialsDTO);
			responseDTO.setServiceResult(loginUserDTO);
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (AccountNotActivatedExeception aae) {
			LoggerUtil.logError(aae);
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
			responseDTO.setMessage(aae.getMessage());
			responseDTO.setServiceResult(aae.getMessage());
		} catch (InvalidLoginException ie) {
			LoggerUtil.logError(ie);
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
			responseDTO.setMessage(ie.getMessage());
			responseDTO.setServiceResult(ie.getMessage());
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while fetching details of user");
			responseDTO.setMessage("Error while fetching details of user");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
			e.printStackTrace();

		}
		return responseDTO;
	}

	// For reset password of user account
	@PostMapping(value = "/resetPassword", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO resetPassword(@RequestBody ResetPasswordDTO resetpassword) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			LoggerUtil.logInfo("inside resetPassword");
			String response = userService.resetPassword(resetpassword);
			responseDTO.setServiceResult(response);
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
			responseDTO.setMessage("password reset successfully.");
		} catch (InvalidAuthCodeException iae) {
			LoggerUtil.logError(iae);
			responseDTO.setServiceResult(iae.getMessage());
			responseDTO.setMessage(iae.getMessage());
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while reseting the password.");
			responseDTO.setMessage("Error while reseting the password.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);

		}
		return responseDTO;
	}

	// for sending forgot password link
	@PostMapping(value = "/forgotPassword", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO forgotPassword(@RequestParam String username) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			LoggerUtil.logInfo("inside forgot");
			String response = userService.forgotPassword(username);
			responseDTO.setServiceResult(response);
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
			responseDTO.setMessage(
					"Email has been sent to your registered email Id. Please follow the steps to reset your password");
		}

		catch (UserNotFoundException ue) {
			LoggerUtil.logError(ue);
			responseDTO.setServiceResult(ue.getMessage());
			responseDTO.setMessage(ue.getMessage());
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}

		catch (Exception e) {

			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while fetching user.");
			responseDTO.setMessage("Error while fetching user.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);

		}
		return responseDTO;
	}

	// for signUp of organization
	@PostMapping(value = "/signUp", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO signUp(@RequestBody SignUpDTO signUpDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			userService.signUp(signUpDTO);
			responseDTO.setServiceResult("Signup done successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while fetching details of user");
			responseDTO.setMessage("Error while fetching details of user");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for activating user account
	@PostMapping(value = "/activateUser", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO activateUser(@RequestParam String activationCode, @RequestParam Integer userId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			String response = userService.activateUser(activationCode, userId);
			responseDTO.setServiceResult(response);
			responseDTO.setMessage(response);
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while activating user account.");
			responseDTO.setMessage("Error while activating user account.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for resend code
	@PostMapping(value = "/resendCode", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO resendCode(@RequestParam Integer userId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			String response = userService.resendCode(userId);
			responseDTO.setServiceResult(response);
			responseDTO.setMessage(response);
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while resending code.");
			responseDTO.setMessage("Error while resending code.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// For fetching latitude and longitude according to zip code
	@GetMapping(value = "/latLon", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchLatLon(@RequestParam Integer zipCode) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			PlaceDTO placeDTO = userService.fetchLatLon(zipCode);

			responseDTO.setServiceResult(placeDTO);
			responseDTO.setMessage("Latitude and Longitude fetched Successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);

		} catch (InvalidZipCodeException ie) {
			LoggerUtil.logError(ie);
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
			responseDTO.setMessage(ie.getMessage());
			responseDTO.setServiceResult(ie.getMessage());
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while fetching lat lon");
			responseDTO.setMessage("Error while fetching lat lon");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// For fetching place list according to latitude and longitude
	@GetMapping(value = "/placeList", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchPlaceList(@RequestParam String place, @RequestParam String latLon,
			@RequestParam String countryCode) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<PlaceDTO> placeList = userService.fetchPlaceList(place, latLon, countryCode);
			responseDTO.setServiceResult(placeList);
			responseDTO.setMessage("Place List fetched successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while fetching place list.");
			responseDTO.setMessage("Error while fetching place list");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// For fetching place details by place Id
	@GetMapping(value = "/placeDetails", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO placeDetails(@RequestParam String placeId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			OrganizationDTO orgDTO = userService.fetchPlaceDetails(placeId);
			responseDTO.setServiceResult(orgDTO);
			responseDTO.setMessage("Place List fetched successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while fetching place details.");
			responseDTO.setMessage("Error while fetching place details.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// For fetching country list
	@GetMapping(value = "/country", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchCountryList() {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<CountryDTO> countryList = userService.fetchCountryList();
			responseDTO.setServiceResult(countryList);
			responseDTO.setMessage("Country List fetched successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while fetching country list.");
			responseDTO.setMessage("Error while fetching country list.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// For create business login.
	@PostMapping(value = "/user", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO addUser(@RequestBody UserSignUpDTO userSignUpDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			Integer userId = userService.addUser(userSignUpDTO);
			responseDTO.setServiceResult(userId);
			responseDTO.setMessage("User Added Successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (DuplicateUserNameExeception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Username/Email already exists. Please try different one.");
			responseDTO.setMessage("Username/Email already exists. Please try different one.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		} catch (DuplicateEmailExeception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Username/Email already exists. Please try different one.");
			responseDTO.setMessage("Username/Email already exists. Please try different one.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while adding user.");
			responseDTO.setMessage("Error while adding user.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;

	}

	// For validating reset password url
	@GetMapping(value = "/validateResetPwdUrl", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO validateResetPwdUrl(@RequestParam Integer userId, @RequestParam String authCode) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			userService.validateResetPasswordUrl(userId, authCode);
			responseDTO.setServiceResult("Url validated successfully.");
			responseDTO.setMessage("Url validated successfully.");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (InvalidPwdUrlException ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult(ex.getMessage());
			responseDTO.setMessage(ex.getMessage());
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while validating reset password url.");
			responseDTO.setMessage("Error while validating reset password url.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// For fetching organization details for given id
	@GetMapping(value = "/organization", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchOrganization(@RequestParam Integer orgId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			OrganizationDTO orgDTO = userService.fetchOrganizationById(orgId);
			responseDTO.setServiceResult(orgDTO);
			responseDTO.setMessage("Organization Details fetched successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while fetching organization details.");
			responseDTO.setMessage("Error while fetching organization details.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
			e.printStackTrace();
		}
		return responseDTO;
	}

	// For updating user account password
	@PutMapping(value = "/password", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO changePassword(@RequestParam String oldPwd, @RequestParam String newPwd,
			@RequestParam Integer userId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			userService.changePassword(oldPwd, newPwd, userId);
			responseDTO.setServiceResult("User Password updated successfully");
			responseDTO.setMessage("User Password updated successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);

		} catch (PasswordNotMatchException pe) {
			LoggerUtil.logError(pe);
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
			responseDTO.setMessage(pe.getMessage());
			responseDTO.setServiceResult(pe.getMessage());
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while updating password");
			responseDTO.setMessage("Error while fetching updating password");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// For updating user profile
	@PutMapping(value = "/profileSetting",consumes="multipart/form-data", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO updateprofileSetting(HttpServletRequest request) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			userService.updateProfileSetting(request);
			responseDTO.setServiceResult("User profile details updated successfully");
			responseDTO.setMessage("User profile details updated successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);

		}catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while updating profile details");
			responseDTO.setMessage("Error while updating profile details");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

}
