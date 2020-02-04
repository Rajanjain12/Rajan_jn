package com.kyobeeWaitlistService.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
import com.kyobeeWaitlistService.dto.ResponseDTO;
import com.kyobeeWaitlistService.service.GuestService;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;

@RestController
@RequestMapping("/rest/guest")
public class GuestController {
	
	@Autowired
	GuestService guestService;

	@GetMapping(value = "/metrics", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO metrics(@RequestParam("guestId") Integer guestId,
			@RequestParam("orgId") Integer orgId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			GuestMetricsDTO metricsDTO = guestService.getGuestMetrics(guestId, orgId);
			responseDTO.setServiceResult(metricsDTO);
			responseDTO.setMessage("Guest metrics fetched successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while fetching guest metrics");
			responseDTO.setMessage("Error while fetching guest metrics");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
	
	@GetMapping(value = "/resetGuestList", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO resetGuests(@RequestParam(value = "orgid") Long orgID) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
  		    guestService.resetOrganizationsByOrgId(orgID);
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
