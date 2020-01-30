package com.kyobeeWaitlistService.service;


public interface WaitListService {
	public void resetOrganizationsByOrgId(Long orgid);
	public void sendPusherMessage(Object rootMap, String channel);

}
