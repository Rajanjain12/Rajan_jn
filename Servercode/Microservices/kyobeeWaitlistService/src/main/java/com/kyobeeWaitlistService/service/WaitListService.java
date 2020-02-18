package com.kyobeeWaitlistService.service;

import java.util.HashMap;

import com.kyobeeWaitlistService.dto.OrgPrefKeyMapDTO;
import com.kyobeeWaitlistService.dto.OrganizationMetricsDTO;
import com.kyobeeWaitlistService.dto.PusherDTO;

public interface WaitListService {
	
	public HashMap<String, Object> updateLanguagesPusher();
	OrganizationMetricsDTO getOrganizationMetrics(Integer orgId);
	public PusherDTO updateOrgSettings(Integer orgId, Integer perPartyWaitTime,Integer numberOfUsers);
	OrgPrefKeyMapDTO fetchOrgPrefandKeyMap(Integer orgId);
	
}
