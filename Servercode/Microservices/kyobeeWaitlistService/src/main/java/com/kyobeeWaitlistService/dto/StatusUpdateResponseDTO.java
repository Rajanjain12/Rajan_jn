package com.kyobeeWaitlistService.dto;

import com.kyobeeWaitlistService.entity.Guest;

public class StatusUpdateResponseDTO {

	private WaitlistMetrics waitlistMetrics;
	private Guest guest;
	private Integer orgId;
	
	public WaitlistMetrics getWaitlistMetrics() {
		return waitlistMetrics;
	}
	public void setWaitlistMetrics(WaitlistMetrics waitlistMetrics) {
		this.waitlistMetrics = waitlistMetrics;
	}
	public Guest getGuest() {
		return guest;
	}
	public void setGuest(Guest guest) {
		this.guest = guest;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	
	
}
