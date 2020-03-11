package com.kyobeeWaitlistService.service;

import java.util.List;

import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
import com.kyobeeWaitlistService.dto.LanguageMasterDTO;
import com.kyobeeWaitlistService.dto.OrganizationTemplateDTO;
import com.kyobeeWaitlistService.dto.SmsContentDTO;

public interface OrganizationTemplateService {
	
	List<OrganizationTemplateDTO> getOrganizationTemplates(SmsContentDTO smsContentDTO);
	List<OrganizationTemplateDTO> getSmsContent(SmsContentDTO smsContentDTO, List<OrganizationTemplateDTO> smsTemplates, GuestMetricsDTO guestMetrics);
	void addLanguage(LanguageMasterDTO langMasterDTO, Integer orgID);
	void deleteLanguage(Integer orgId,Integer langId);

}
