package com.kyobeeUserService.service;



import com.kyobeeUserService.dto.CredentialsDTO;
import com.kyobeeUserService.dto.LoginUserDTO;
import com.kyobeeUserService.dto.ResetPasswordDTO;
import com.kyobeeUserService.util.Exception.AccountNotActivatedExeception;
import com.kyobeeUserService.util.Exception.InvalidAuthCodeException;
import com.kyobeeUserService.util.Exception.InvalidLoginException;
import com.kyobeeUserService.util.Exception.UserNotFoundException;

public interface UserService {

	
	public LoginUserDTO logInCredentialValidate(CredentialsDTO credentialsDTO) throws InvalidLoginException,AccountNotActivatedExeception;
	
	public String resetPassword(ResetPasswordDTO resetPasswordDTO) throws InvalidAuthCodeException;
	
	public String forgotPassword(String username) throws UserNotFoundException;
}
