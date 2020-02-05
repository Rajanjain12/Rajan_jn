package com.kyobeeWaitlistService.service;

import com.kyobeeWaitlistService.dto.AddUpdateGuestDTO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestHistoryRequestDTO;
import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
import com.kyobeeWaitlistService.dto.GuestResponseDTO;

public interface GuestService {

	public GuestMetricsDTO getGuestMetrics(Integer guestId, Integer orgId);
	public void resetOrganizationsByOrgId(Long orgid);
	public GuestResponseDTO fetchGuestList(Integer orgId,Integer pageSize,Integer pageNo,String searchText);
	//for fetching guest history list
	public GuestResponseDTO fetchGuestHistoryList(GuestHistoryRequestDTO guestRequest);
	//for add or update guest
	public AddUpdateGuestDTO addOrUpdateGuest(GuestDTO guestDTO);

}
