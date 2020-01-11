package com.kyobeeUserService.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeUserService.dto.ResponseDTO;
import com.kyobeeUserService.service.UserService;
import com.kyobeeUserService.util.UserServiceConstants;
import com.kyobeeUserService.util.Exception.AccountNotActivatedExeception;
import com.kyobeeUserService.util.Exception.InvalidAuthCodeException;
import com.kyobeeUserService.util.Exception.InvalidLoginException;
import com.kyobeeUserService.util.Exception.UserNotFoundException;
import com.kyobeeUserService.dto.CredentialsDTO;
import com.kyobeeUserService.dto.LoginUserDTO;
import com.kyobeeUserService.dto.ResetPasswordDTO;

@CrossOrigin
@RestController
@RequestMapping("/rest/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseDTO login(@RequestBody CredentialsDTO credentialsDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			System.out.println("login method");
			LoginUserDTO loginUserDTO= userService.logInCredentialValidate(credentialsDTO);
			responseDTO.setServiceResult(loginUserDTO);
			responseDTO.setSuccess(UserServiceConstants.SUSSESS_CODE);
		}
		catch(AccountNotActivatedExeception aae) {
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
			responseDTO.setMessage(aae.getMessage());
			responseDTO.setServiceResult(aae.getMessage());
		}
		catch(InvalidLoginException ie) {
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
			responseDTO.setMessage(ie.getMessage());
			responseDTO.setServiceResult(ie.getMessage());
		}
		catch (Exception e) {
			System.out.println(e);
			responseDTO.setServiceResult("Error while fetching details of user");
			responseDTO.setMessage("Error while fetching details of user");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);

		}
		return responseDTO;
	}
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseDTO resetPassword(@RequestBody ResetPasswordDTO resetpassword) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			
			String response= userService.resetPassword(resetpassword);
			responseDTO.setServiceResult(response);
			responseDTO.setSuccess(UserServiceConstants.SUSSESS_CODE);
			responseDTO.setMessage("password reset successfully.");
		}
		catch(InvalidAuthCodeException iae) {
			
			responseDTO.setServiceResult(iae.getMessage());
			responseDTO.setMessage(iae.getMessage());
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		catch (Exception e) {
				System.out.println(e);
			responseDTO.setServiceResult("Error while reseting the password.");
			responseDTO.setMessage("Error while reseting the password.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);

		}
		return responseDTO;
	}
    
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
   	public @ResponseBody ResponseDTO forgotPassword(@RequestParam String username) {
     
    	System.out.println("inside forgot");
   		ResponseDTO responseDTO = new ResponseDTO();
   		try {
   			
   			String response= userService.forgotPassword(username);
   			responseDTO.setServiceResult(response);
   			responseDTO.setSuccess(UserServiceConstants.SUSSESS_CODE);
   			responseDTO.setMessage("Email has been sent to your registered email Id. Please follow the steps to reset your password");
   		}
		
		  catch(UserNotFoundException ue) {
		  
		  responseDTO.setServiceResult(ue.getMessage());
		  responseDTO.setMessage(ue.getMessage());
		  responseDTO.setSuccess(UserServiceConstants.ERROR_CODE); }
		 
   		catch (Exception e) {
   			
   			System.out.println(e);
   			responseDTO.setServiceResult("Error while fetching user.");
   			responseDTO.setMessage("Error while fetching user.");
   			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);

   		}
   		return responseDTO;
   	}

}
