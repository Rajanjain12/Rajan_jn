package com.kyobeeWaitlistService.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kyobeeWaitlistService.entity.Guest;

@Repository
public interface GuestCustomDAO {
	
	public List<Guest> fetchAllGuestHistoryList(Integer orgId,Integer recordsPerPage,Integer pageNumber,String statusOption, String clientTimezone,Integer sliderMinValue,Integer sliderMaxValue,String searchText);

}
