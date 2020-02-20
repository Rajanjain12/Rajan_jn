package com.kyobeeWaitlistService.service;

import java.util.HashMap;

import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.OrgPrefKeyMapDTO;
import com.kyobeeWaitlistService.dto.OrganizationMetricsDTO;
import com.kyobeeWaitlistService.dto.OrganizationTemplateDTO;
import com.kyobeeWaitlistService.dto.PusherDTO;
import com.kyobeeWaitlistService.dto.SendSMSDTO;


public interface WaitListService {
	
	public HashMap<String, Object> updateLanguagesPusher();
	OrganizationMetricsDTO getOrganizationMetrics(Integer orgId);
	public PusherDTO updateOrgSettings(Integer orgId, Integer perPartyWaitTime,Integer numberOfUsers);
	OrgPrefKeyMapDTO fetchOrgPrefandKeyMap(Integer orgId);
	void getSMSDetails(GuestDTO guestDTO, SendSMSDTO sendSMSDTO);
	OrganizationTemplateDTO getOrganizationTemplateByLevel(GuestDTO guestDTO,SendSMSDTO sendSMSDTO);
	SendSMSDTO getSmsContentByLevel(GuestDTO guestDTO, OrganizationTemplateDTO smsTemplate, OrganizationMetricsDTO orgMetricsDTO);
	void saveSmsLog(GuestDTO guestDTO,SendSMSDTO sendSMSDTO);
	
}
