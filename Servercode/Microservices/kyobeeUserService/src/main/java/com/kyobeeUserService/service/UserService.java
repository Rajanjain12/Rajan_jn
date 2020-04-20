package com.kyobeeUserService.service;



import java.util.List;

import org.json.JSONException;

import com.kyobeeUserService.dto.CredentialsDTO;
import com.kyobeeUserService.dto.LoginUserDTO;
import com.kyobeeUserService.dto.OrganizationDTO;
import com.kyobeeUserService.dto.PlaceDTO;
import com.kyobeeUserService.dto.ResetPasswordDTO;
import com.kyobeeUserService.dto.ResponseDTO;
import com.kyobeeUserService.dto.SignUpDTO;
import com.kyobeeUserService.util.Exception.InvalidActivationCodeException;
import com.kyobeeUserService.util.Exception.AccountNotActivatedExeception;
import com.kyobeeUserService.util.Exception.InvalidAuthCodeException;
import com.kyobeeUserService.util.Exception.InvalidLoginException;
import com.kyobeeUserService.util.Exception.UserNotFoundException;

public interface UserService {

	//for authenticating user by username and password 
	public LoginUserDTO logInCredentialValidate(CredentialsDTO credentialsDTO) throws InvalidLoginException,AccountNotActivatedExeception;

	public String resetPassword(ResetPasswordDTO resetPasswordDTO) throws InvalidAuthCodeException;
	
	public String forgotPassword(String username) throws UserNotFoundException;
	
	public void signUp(SignUpDTO signUpDTO);
	
	public Boolean checkIfUserExist(String Email);
	
	public String activateUser(String activationCode,Integer userId) throws InvalidActivationCodeException;
	
	public String resendCode(Integer userId);
	
	public ResponseDTO fetchLatLon(Integer zipCode) throws JSONException;
	
	public List<PlaceDTO> fetchPlaceList(String place,String latLon) throws JSONException;
	
	public OrganizationDTO fetchPlaceDetails(String placeId) throws JSONException;
	
	public List<String> fetchCountryList();

}