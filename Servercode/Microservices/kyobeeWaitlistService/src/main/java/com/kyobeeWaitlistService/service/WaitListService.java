package com.kyobeeWaitlistService.service;

import java.util.HashMap;
import com.kyobeeWaitlistService.dto.OrganizationMetricsDTO;

public interface WaitListService {
	
	public HashMap<String, Object> updateLanguagesPusher();
	OrganizationMetricsDTO getOrganizationMetrics(Integer orgId);

}
