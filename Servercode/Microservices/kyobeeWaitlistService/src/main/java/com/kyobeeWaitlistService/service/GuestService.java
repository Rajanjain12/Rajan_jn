package com.kyobeeWaitlistService.service;

import com.kyobeeWaitlistService.dto.AddUpdateGuestDTO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestMarketingPreferenceDTO;
import com.kyobeeWaitlistService.dto.GuestMetricsDTO;
import com.kyobeeWaitlistService.dto.GuestResponseDTO;
import com.kyobeeWaitlistService.dto.GuestWebDTO;
import com.kyobeeWaitlistService.util.Exeception.InvalidGuestException;

public interface GuestService {

	//fetch guest metrics
	public GuestMetricsDTO getGuestMetrics(Integer guestId, Integer orgId);
	// reset guest list for organization
	public void resetOrganizationsByOrgId(Long orgid);
	public GuestResponseDTO fetchGuestList(Integer orgId,Integer pageSize,Integer pageNo,String searchText);
	//for fetching guest history list
	public GuestResponseDTO fetchGuestHistoryList(Integer orgId,Integer pageSize,Integer pageNo,String searchText,String clientTimezone,Integer sliderMaxTime,Integer sliderMinTime,String statusOption);
	//for add guest
	public AddUpdateGuestDTO addGuest(GuestDTO guestDTO);
	//for update Guest
	public AddUpdateGuestDTO updateGuestDetails(GuestDTO guestDTO);
	//for mark as seated,delete,incomplete,not present
	public void updateGuestStatus(Integer guestId,Integer orgId,String status) throws InvalidGuestException;
	//fetch guest detail by id or uuid
	public GuestDTO fetchGuestDetails(Integer guestID, String guestUUID) throws InvalidGuestException;
	//fetch guest by contact number
	public GuestDTO fetchGuestByContact(Integer orgID, String contactNo) throws InvalidGuestException;
	//add marketing pref for mobile
	public void addMarketingPref(GuestMarketingPreferenceDTO marketingPrefDTO);
	//add language key map to guest dto for guest detail page
	public GuestWebDTO fetchguestDetails(String guestUUID) throws InvalidGuestException;

}
