package com.kyobeeWaitlistService.service;

import java.util.List;

import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
import com.kyobeeWaitlistService.dto.OrgSettingDTO;
import com.kyobeeWaitlistService.dto.OrganizationTemplateDTO;
import com.kyobeeWaitlistService.dto.SmsContentDTO;

public interface OrganizationTemplateService {
	//fetch organization sms template
	public List<OrganizationTemplateDTO> getOrganizationTemplates(SmsContentDTO smsContentDTO);
	//fetch sms content for guest
	public List<OrganizationTemplateDTO> getSmsContent(SmsContentDTO smsContentDTO, List<OrganizationTemplateDTO> smsTemplates, GuestMetricsDTO guestMetrics);
	//add particular language for an organization
	public OrgSettingDTO addLanguage(Integer langId, Integer orgId);
	//remove language from organization
	public void deleteLanguage(Integer orgId,Integer langId);
	//fetch sms content for guest by fetching organization template then filling guest specific data in variable
	public List<OrganizationTemplateDTO> fetchSmsContentByOrganizationTeplates(SmsContentDTO smsContentDTO);

}
