package com.kyobeeWaitlistService.dao;

import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.dto.OrganizationMetricsDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;

@Transactional
public interface OrganizationCustomDAO {
	
	public WaitlistMetrics getOrganizationMetrics(Integer orgId);
	public WaitlistMetrics updateOrgSettings(Integer orgId, Integer perPartyWaitTime, Integer numberOfUsers);
	public void resetOrganizationByOrgId(Long orgId);
}
