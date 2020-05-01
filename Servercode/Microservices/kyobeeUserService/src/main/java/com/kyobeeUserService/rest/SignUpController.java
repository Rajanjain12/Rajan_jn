package com.kyobeeUserService.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeUserService.dto.OrganizationDTO;
import com.kyobeeUserService.dto.OrganizationTypeDTO;
import com.kyobeeUserService.dto.ResponseDTO;
import com.kyobeeUserService.service.SignUpService;
import com.kyobeeUserService.util.LoggerUtil;
import com.kyobeeUserService.util.UserServiceConstants;

@CrossOrigin
@RestController
@RequestMapping("/rest/user/signup")
public class SignUpController {

	@Autowired
	SignUpService signUpService;

	//for adding new business
	@PostMapping(value = "/business", consumes = "application/json", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO addBusiness(@RequestBody OrganizationDTO organizationDTO) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			signUpService.addBusiness(organizationDTO);
			responseDTO.setServiceResult("Business added successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while saving business.");
			responseDTO.setMessage("Error while saving business.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for fetching organization type
	@GetMapping(value = "/organizationType", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchOrganizationType() {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<OrganizationTypeDTO> orgTypeDTO = signUpService.fetchAllOrganizationType();
			responseDTO.setServiceResult(orgTypeDTO);
			responseDTO.setMessage("Organization type fetched successfully");
			responseDTO.setSuccess(UserServiceConstants.SUCCESS_CODE);
		} catch (Exception e) {
			LoggerUtil.logError(e);
			responseDTO.setServiceResult("Error while fetching organization type.");
			responseDTO.setMessage("Error while fetching organization type.");
			responseDTO.setSuccess(UserServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
}
