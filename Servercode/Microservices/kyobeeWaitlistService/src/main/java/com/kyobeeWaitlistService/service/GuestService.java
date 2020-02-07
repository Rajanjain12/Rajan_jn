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
	//for add guest
	public AddUpdateGuestDTO addGuest(GuestDTO guestDTO);
	//for update Guest
	public AddUpdateGuestDTO updateGuestDetails(GuestDTO guestDTO);
	//for mark as seated,delete,incomplete,not present
	public void updateGuestStatus(Integer guestId,Integer orgId,String status);

}
