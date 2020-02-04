package com.kyobeeWaitlistService.rest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeWaitlistService.dto.OrganizationMetricsDTO;
import com.kyobeeWaitlistService.dto.ResponseDTO;
import com.kyobeeWaitlistService.service.WaitListService;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;

@RestController
@RequestMapping("/rest/waitlist")
public class WaitListController {

	@Autowired
	WaitListService waitListService;

	@GetMapping(value = "/refreshLanguage", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO refreshLanguage() {
		HashMap<String, Object> rootMap = new LinkedHashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			rootMap = waitListService.updateLanguagesPusher();
			responseDTO.setServiceResult(rootMap);
			responseDTO.setMessage("Language Refreshed Successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while refreshing language");
			responseDTO.setMessage("Error while refreshing language");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}


	@GetMapping(value = "/organizationMetrics", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO organizationMetrics(
			@RequestParam(value = "orgId") Integer orgId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			OrganizationMetricsDTO orgMetricsDTO = waitListService.getOrganizationMetrics(orgId);
			responseDTO.setServiceResult(orgMetricsDTO);
			responseDTO.setMessage("Organization metrics fetched successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while fetching organization metrics");
			responseDTO.setMessage("Error while fetching organization metrics");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

}
