package com.kyobeeWaitlistService.dao;

import java.util.List;

import com.kyobeeWaitlistService.dto.AddUpdateGuestDTO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestDetailsDTO;
import com.kyobeeWaitlistService.dto.GuestHistoryRequestDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;
import com.kyobeeWaitlistService.entity.Guest;


public interface GuestCustomDAO {
	
	public List<Guest> fetchAllGuestHistoryList(GuestHistoryRequestDTO guestRequest);
	
	public AddUpdateGuestDTO addGuest(GuestDTO guestDTO,String seatingPref,String marketingPref);
	
	public AddUpdateGuestDTO updateGuestDetails(GuestDTO guestDTO,String seatingPref,String marketingPref);
	
	public WaitlistMetrics updateGuestStatus(Integer guestId,Integer orgId,String status);
	
	public List<GuestDetailsDTO> fetchGuestByContact(Integer orgID, String contactNumber);
	
	
	

}
