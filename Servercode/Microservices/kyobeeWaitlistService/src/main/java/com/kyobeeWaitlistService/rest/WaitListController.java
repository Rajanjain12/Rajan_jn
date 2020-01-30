package com.kyobeeWaitlistService.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeWaitlistService.dto.ResponseDTO;
import com.kyobeeWaitlistService.service.WaitListService;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;

@RestController
@RequestMapping("/rest/waitlist")
public class WaitListController {

	@Autowired
	WaitListService waitListService;

	 @RequestMapping(value = "/reset", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseDTO resetGuests(@RequestParam(value = "orgid", required = true) Long orgID) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {

			waitListService.resetOrganizationsByOrgId(orgID);
			responseDTO.setServiceResult("Reset Successfully");
			responseDTO.setMessage("Reset Successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("System Error - resetOrganization failed");
			responseDTO.setMessage("System Error - resetOrganization failed");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
}
