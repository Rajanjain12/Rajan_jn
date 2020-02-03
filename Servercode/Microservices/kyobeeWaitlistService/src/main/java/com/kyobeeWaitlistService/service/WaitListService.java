package com.kyobeeWaitlistService.service;

import java.util.List;

import com.kyobeeWaitlistService.dto.GuestResponseDTO;

public interface WaitListService {
	public void resetOrganizationsByOrgId(Long orgid);
	public void sendPusherMessage(Object rootMap, String channel);
	public GuestResponseDTO fetchGuestList(Integer orgId,Integer pageSize,Integer pageNo);

}
