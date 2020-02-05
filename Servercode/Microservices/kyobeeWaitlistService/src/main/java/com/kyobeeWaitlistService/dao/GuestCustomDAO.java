package com.kyobeeWaitlistService.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kyobeeWaitlistService.dto.AddUpdateGuestDTO;
import com.kyobeeWaitlistService.dto.GuestDTO;
import com.kyobeeWaitlistService.dto.GuestHistoryRequestDTO;
import com.kyobeeWaitlistService.entity.Guest;

@Repository
public interface GuestCustomDAO {
	
	public List<Guest> fetchAllGuestHistoryList(GuestHistoryRequestDTO guestRequest);
	
	public AddUpdateGuestDTO addGuest(GuestDTO guestDTO,String seatingPref,String marketingPref);

}
