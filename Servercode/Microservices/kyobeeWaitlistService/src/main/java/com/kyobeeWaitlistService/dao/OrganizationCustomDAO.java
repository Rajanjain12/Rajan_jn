package com.kyobeeWaitlistService.dao;

import com.kyobeeWaitlistService.dto.OrganizationMetricsDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;

public interface OrganizationCustomDAO {
	
	public OrganizationMetricsDTO getOrganizationMetrics(Integer orgId);
	public WaitlistMetrics updateOrgSettings(Integer orgId, Integer perPartyWaitTime, Integer numberOfUsers);
	public void resetOrganizationByOrgId(Long orgId);
}
