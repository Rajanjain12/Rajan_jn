package com.kyobeeAccountService.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeAccountService.dto.OrganizationDTO;
import com.kyobeeAccountService.dto.ResponseDTO;
import com.kyobeeAccountService.service.AccountService;
import com.kyobeeAccountService.util.AccountServiceConstants;
import com.kyobeeAccountService.util.LoggerUtil;
import com.kyobeeAccountService.util.Exception.PasswordNotMatchException;

@CrossOrigin
@RestController
@RequestMapping("/rest/account")
public class AccountController {

	@Autowired
	AccountService accountService;

	// For fetching account details
	@GetMapping(value = "/", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchAccountDetails(@RequestParam Integer orgId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			OrganizationDTO orgDTO = accountService.fetchAccountDetails(orgId);
			responseDTO.setServiceResult(orgDTO);
			responseDTO.setMessage("Organization Details fetched successfully");
			responseDTO.setSuccess(AccountServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while fetching organization details.");
			responseDTO.setMessage("Error while fetching organization details.");
			responseDTO.setSuccess(AccountServiceConstants.ERROR_CODE);
			e.printStackTrace();
		}
		return responseDTO;
	}

	// For updating user profile
	@PutMapping(value = "/", consumes = "multipart/form-data", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO updateAccountDetails(HttpServletRequest request) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			accountService.updateAccountDetails(request);
			responseDTO.setServiceResult("User profile details updated successfully");
			responseDTO.setMessage("User profile details updated successfully");
			responseDTO.setSuccess(AccountServiceConstants.SUCCESS_CODE);

		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while updating profile details");
			responseDTO.setMessage("Error while updating profile details");
			responseDTO.setSuccess(AccountServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// For updating user account password
	@PutMapping(value = "/password", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO updatePassword(@RequestParam String oldPwd, @RequestParam String newPwd,
			@RequestParam Integer userId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			accountService.updatePassword(oldPwd, newPwd, userId);
			responseDTO.setServiceResult("User Password updated successfully");
			responseDTO.setMessage("User Password updated successfully");
			responseDTO.setSuccess(AccountServiceConstants.SUCCESS_CODE);

		} catch (PasswordNotMatchException pe) {
			LoggerUtil.logError(pe);
			responseDTO.setSuccess(AccountServiceConstants.ERROR_CODE);
			responseDTO.setMessage(pe.getMessage());
			responseDTO.setServiceResult(pe.getMessage());
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while updating password");
			responseDTO.setMessage("Error while fetching updating password");
			responseDTO.setSuccess(AccountServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// For deleting user
	@DeleteMapping(value = "/", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO deleteAccount(@RequestParam Integer orgId, @RequestParam Integer userId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			accountService.deleteAccount(orgId, userId);
			responseDTO.setServiceResult("Resturant deleted successfully");
			responseDTO.setMessage("Resturant deleted successfully");
			responseDTO.setSuccess(AccountServiceConstants.SUCCESS_CODE);

		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while deleting resturant");
			responseDTO.setMessage("Error while deleting resturant");
			responseDTO.setSuccess(AccountServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

}
