package com.kyobeeWaitlistService.service;

import java.util.HashMap;
import java.util.List;

import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.LanguageKeyMappingDTO;
import com.kyobeeWaitlistService.dto.OrgPrefKeyMapDTO;
import com.kyobeeWaitlistService.dto.OrgSettingDTO;
import com.kyobeeWaitlistService.dto.OrganizationTemplateDTO;
import com.kyobeeWaitlistService.dto.PusherDTO;
import com.kyobeeWaitlistService.dto.SeatingMarketingPrefDTO;
import com.kyobeeWaitlistService.dto.SendSMSDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;
import com.kyobeeWaitlistService.entity.Organization;
import com.kyobeeWaitlistService.util.Exeception.InvalidGuestException;


public interface WaitListService {
	
	public HashMap<String, Object> updateLanguagesPusher();
	WaitlistMetrics getOrganizationMetrics(Integer orgId);
	public PusherDTO updateOrgSettings(Integer orgId, Integer perPartyWaitTime,Integer numberOfUsers);
	OrgPrefKeyMapDTO fetchOrgPrefandKeyMap(Integer orgId);
	void getSMSDetails(GuestDTO guestDTO, SendSMSDTO sendSMSDTO);
	OrganizationTemplateDTO getOrganizationTemplateByLevel(GuestDTO guestDTO,SendSMSDTO sendSMSDTO);
	SendSMSDTO getSmsContentByLevel(GuestDTO guestDTO, OrganizationTemplateDTO smsTemplate, WaitlistMetrics waitlistMetrics);
	void saveSmsLog(GuestDTO guestDTO,SendSMSDTO sendSMSDTO);
	void sendSMS(SendSMSDTO sendSMSDTO) throws InvalidGuestException;
	void updateOrgSetting(OrgSettingDTO orgSettingDTO);
	OrgSettingDTO fetchOrgSetting();
	List<LanguageKeyMappingDTO> fetchOrgLangKeyMap(Integer orgId);
	
}
