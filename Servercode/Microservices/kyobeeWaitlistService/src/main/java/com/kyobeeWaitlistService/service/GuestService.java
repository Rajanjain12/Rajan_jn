package com.kyobeeWaitlistService.service;

import com.kyobeeWaitlistService.dto.GuestMetricsDTO;

public interface GuestService {

	public GuestMetricsDTO getGuestMetrics(Integer guestId, Integer orgId);
	public void resetOrganizationsByOrgId(Long orgid);
}
