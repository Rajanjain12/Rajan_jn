package com.kyobeeWaitlistService.service;

import java.util.HashMap;
import com.kyobeeWaitlistService.dto.OrganizationMetricsDTO;
import com.kyobeeWaitlistService.dto.WaitListMetricsDTO;

public interface WaitListService {
	
	public HashMap<String, Object> updateLanguagesPusher();
	OrganizationMetricsDTO getOrganizationMetrics(Integer orgId);
	public WaitListMetricsDTO updateOrgSettings(Integer orgId, Integer perPartyWaitTime,Integer numberOfUsers);
	
}
