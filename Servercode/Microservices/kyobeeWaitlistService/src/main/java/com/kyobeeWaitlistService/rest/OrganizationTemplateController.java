package com.kyobeeWaitlistService.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
import com.kyobeeWaitlistService.dto.LanguageMasterDTO;
import com.kyobeeWaitlistService.dto.OrganizationTemplateDTO;
import com.kyobeeWaitlistService.dto.ResponseDTO;
import com.kyobeeWaitlistService.dto.SmsContentDTO;
import com.kyobeeWaitlistService.service.GuestService;
import com.kyobeeWaitlistService.service.OrganizationTemplateService;
import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;

@RestController
@CrossOrigin
@RequestMapping("/rest/waitlist/organizationTemplate")
public class OrganizationTemplateController {

	@Autowired
	OrganizationTemplateService organizationTemplateService;

	@Autowired
	GuestService guestService;

	@PostMapping(value = "/smsContent", produces = "application/vnd.kyobee.v1+json", consumes = "application/json")
	public @ResponseBody ResponseDTO smsContent(@RequestBody SmsContentDTO smsContentDTO) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<OrganizationTemplateDTO> smsTemplates = organizationTemplateService
					.getOrganizationTemplates(smsContentDTO);
			GuestMetricsDTO metricsDTO = guestService.getGuestMetrics(smsContentDTO.getGuestId(),
					smsContentDTO.getOrgId());
			List<OrganizationTemplateDTO> smsContents = organizationTemplateService.getSmsContent(smsContentDTO, smsTemplates,
					metricsDTO);
			LoggerUtil.logInfo(smsContents.toString());
			responseDTO.setServiceResult(smsContents);
			responseDTO.setMessage("Sms content fetched successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);
		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while fetching sms content");
			responseDTO.setMessage("Error while fetching sms content");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
	
	//For adding organization language
	@PostMapping(value = "/language", produces = "application/vnd.kyobee.v1+json", consumes = "application/json")
	public @ResponseBody ResponseDTO addLanguage(@RequestBody LanguageMasterDTO languageMasterDTO, @RequestParam("orgId") Integer orgId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			
			organizationTemplateService.addLanguage(languageMasterDTO,orgId);
			responseDTO.setServiceResult("Organization lang added successfully");
			responseDTO.setMessage("Organization lang added successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);
		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while adding language");
			responseDTO.setMessage("Error while adding language");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
	//For deleting organization language
	@DeleteMapping(value = "/language", produces = "application/vnd.kyobee.v1+json")
	public @ResponseBody ResponseDTO deleteLanguage(@RequestParam("orgId") Integer orgId,@RequestParam("langId") Integer langId) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			
			organizationTemplateService.deleteLanguage(orgId, langId);
			responseDTO.setServiceResult("Organization lang deleted successfully");
			responseDTO.setMessage("Organization lang deleted successfully");
			responseDTO.setSuccess(WaitListServiceConstants.SUCCESS_CODE);
		} catch (Exception ex) {
			LoggerUtil.logError(ex);
			responseDTO.setServiceResult("Error while deleting language");
			responseDTO.setMessage("Error while deleting language");
			responseDTO.setSuccess(WaitListServiceConstants.ERROR_CODE);
		}
		return responseDTO;
	}
}
