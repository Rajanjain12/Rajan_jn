package com.kyobeeWaitlistService.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestDetailsDTO;
import com.kyobeeWaitlistService.dto.WaitlistMetrics;
import com.kyobeeWaitlistService.entity.Guest;

@Transactional
public interface GuestCustomDAO {
	
	public List<Guest> fetchAllGuestHistoryList(Integer orgId,Integer pageSize,Integer startIndex,String searchText,String clientTimezone,Integer sliderMaxTime,Integer sliderMinTime,String statusOption);
	
	public WaitlistMetrics addGuest(GuestDTO guestDTO,String seatingPref);
	public Integer fetchAllGuestHistoryListCount(Integer orgId,String searchText,String clientTimezone,Integer sliderMaxTime,Integer sliderMinTime,String statusOption);
	
	public List<Guest> fetchAllGuestList(Integer orgId,Integer pageSize,Integer startIndex,String searchText);
	
	public Integer fetchAllGuestListCount(Integer orgId,String searchText);
	
	public WaitlistMetrics updateGuestDetails(GuestDTO guestDTO,String seatingPref);
	
	public WaitlistMetrics updateGuestStatus(Integer guestId,Integer orgId,String status);
	
	public List<GuestDetailsDTO> fetchGuestByContact(Integer orgID, String contactNumber);
	
	
	

}
