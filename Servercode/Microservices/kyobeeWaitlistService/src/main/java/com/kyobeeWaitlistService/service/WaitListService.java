package com.kyobeeWaitlistService.service;

import java.text.ParseException;
import java.util.List;

import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.LanguageKeyMappingDTO;
import com.kyobeeWaitlistService.dto.OrgPrefKeyMapDTO;
import com.kyobeeWaitlistService.dto.OrgSettingDTO;
import com.kyobeeWaitlistService.dto.OrganizationTemplateDTO;
import com.kyobeeWaitlistService.dto.PusherDTO;
import com.kyobeeWaitlistService.dto.SendSMSDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;
import com.kyobeeWaitlistService.util.Exeception.InvalidGuestException;


public interface WaitListService {
	
	//for sending pusher on language key map change
	public String updateLanguagesPusher();
	//for fetching organization metrics details
	public WaitlistMetrics getOrganizationMetrics(Integer orgId);
	//to update org setting details
	public PusherDTO updateOrgSettings(Integer orgId, Integer perPartyWaitTime,Integer numberOfUsers);
	// fetch organization key map and pref
	public OrgPrefKeyMapDTO fetchOrgPrefandKeyMap(Integer orgId);
	//fetch set sms detail and send sms
	public void setSMSContentAndSendSMS(GuestDTO guestDTO, SendSMSDTO sendSMSDTO);
	//for fetch org sms template by level
	public OrganizationTemplateDTO getOrganizationTemplateByLevel(GuestDTO guestDTO,SendSMSDTO sendSMSDTO);
	//for fetch sms content by sms level
	public SendSMSDTO getSmsContentByLevel(GuestDTO guestDTO, OrganizationTemplateDTO smsTemplate, WaitlistMetrics waitlistMetrics);
	//for save sms log while send sms
	public void saveSmsLog(GuestDTO guestDTO,SendSMSDTO sendSMSDTO) throws ParseException;
	//for sending sms
	public void sendSMS(SendSMSDTO sendSMSDTO) throws InvalidGuestException;
	//for updating org setting 
	public void updateOrgSetting(OrgSettingDTO orgSettingDTO);
	//for fetching setting related to org
	public OrgSettingDTO fetchOrgSetting();
	//for fetching language key map related to organization
	public List<LanguageKeyMappingDTO> fetchOrgLangKeyMap(Integer orgId);
	
}
