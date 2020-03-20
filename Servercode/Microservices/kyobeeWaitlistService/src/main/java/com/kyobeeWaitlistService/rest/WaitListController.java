package com.kyobeeWaitlistService.rest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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

import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.LanguageKeyMappingDTO;
import com.kyobeeWaitlistService.dto.OrgPrefKeyMapDTO;
import com.kyobeeWaitlistService.dto.OrgSettingDTO;
import com.kyobeeWaitlistService.dto.OrganizationTemplateDTO;
import com.kyobeeWaitlistService.dto.PusherDTO;
import com.kyobeeWaitlistService.dto.ResponseDTO;
import com.kyobeeWaitlistService.dto.SendSMSDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;
import com.kyobeeWaitlistService.service.GuestService;
import com.kyobeeWaitlistService.service.WaitListService;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;

@RestController
@CrossOrigin
@RequestMapping("/rest/waitlist")
public class WaitListController {

	@Autowired
	WaitListService waitListService;

	@Autowired
	GuestService guestService;

	// for sending pusher while there is change language key or value
	@PutMapping(value = "/refreshLanguage", produces = "application/vnd.kyobee.v1+json")
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

	// for fetching organization matrix related details
	@GetMapping(value = "/organizationMetrics", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO getOrganizationMetrics(@RequestParam(value = "orgId") Integer orgId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			WaitlistMetrics waitlistMetricsDTO = waitListService.getOrganizationMetrics(orgId);
			responseDTO.setServiceResult(waitlistMetricsDTO);
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

	// for updating organization metrics setting
	@PutMapping(value = "/orgSetting", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO orgSetting(@RequestParam("orgId") Integer orgId,
			@RequestParam("perPartyWaitTime") Integer perPartyWaitTime,
			@RequestParam("numberOfUsers") Integer numberOfUsers) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			PusherDTO pusherDTO = waitListService.updateOrgSettings(orgId, perPartyWaitTime, numberOfUsers);
			responseDTO.setServiceResult(pusherDTO);
			responseDTO.setMessage("organization setting updated Successfully.");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while updating organization setting");
			responseDTO.setMessage("Error while updating organization setting");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// For fetching seating pref and marketing pref associated with org
	@GetMapping(value = "/orgPref", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchOrgPrefandKeyMap(@RequestParam(value = "orgId") Integer orgId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			OrgPrefKeyMapDTO orgPrefKeyMapDTO = waitListService.fetchOrgPrefandKeyMap(orgId);
			responseDTO.setServiceResult(orgPrefKeyMapDTO);
			responseDTO.setMessage("Organization Preference Key Map fetched successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while fetching organization preference key map");
			responseDTO.setMessage("Error while fetching organization preference key map");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for sending sms
	@PostMapping(value = "/sendSMS", produces = "application/vnd.kyobee.v1+json", consumes = "application/json")
	public @ResponseBody ResponseDTO sendSMS(@RequestBody SendSMSDTO sendSMSDTO) {
		
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			waitListService.sendSMS(sendSMSDTO);
			responseDTO.setServiceResult("Sms sent successfully");
			responseDTO.setMessage("Sms sent successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while sending sms");
			responseDTO.setMessage("Error while sending sms");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

	// for updating organization setting
	@PutMapping(value = "/setting", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO updateOrgSetting(@RequestBody OrgSettingDTO orgSettingDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			
            waitListService.updateOrgSetting(orgSettingDTO);
			responseDTO.setServiceResult("organization setting updated Successfully.");
			responseDTO.setMessage("organization setting updated Successfully.");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while updating organization setting");
			responseDTO.setMessage("Error while updating organization setting");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
	
	// for fetching organization setting
	@GetMapping(value = "/setting", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchOrgSetting() {

		ResponseDTO responseDTO = new ResponseDTO();
		try {

			OrgSettingDTO orgSettingDTO = waitListService.fetchOrgSetting();
			responseDTO.setServiceResult(orgSettingDTO);
			responseDTO.setMessage("Successfully fetched organization setting.");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while fetching organization setting");
			responseDTO.setMessage("Error while fetching organization setting");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
	
	// For fetching seating language key map associated with org
	@GetMapping(value = "/orgLangKeyMap", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO fetchOrgLangKeyMap(@RequestParam(value = "orgId") Integer orgId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<LanguageKeyMappingDTO> orgKeyMapDTO = waitListService.fetchOrgLangKeyMap(orgId);
			responseDTO.setServiceResult(orgKeyMapDTO);
			responseDTO.setMessage("Organization Language Key Map fetched successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);

		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while fetching organization Language key map");
			responseDTO.setMessage("Error while fetching organization Language key map");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}

}
