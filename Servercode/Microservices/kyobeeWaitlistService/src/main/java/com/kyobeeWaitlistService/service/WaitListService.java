package com.kyobeeWaitlistService.service;

import java.util.HashMap;
import com.kyobeeWaitlistService.dto.OrganizationMetricsDTO;
import java.util.List;

import com.kyobeeWaitlistService.dto.GuestResponseDTO;

public interface WaitListService {
	
	public HashMap<String, Object> updateLanguagesPusher();
	OrganizationMetricsDTO getOrganizationMetrics(Integer orgId);
	
}
