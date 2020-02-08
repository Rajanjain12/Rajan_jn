package com.kyobeeWaitlistService.dto;

import java.util.List;

public class GuestMarketingPreferenceDTO {
	
	private Integer guestID;
	private List<SeatingMarketingPrefDTO> marketingPreference;
	
	public Integer getGuestID() {
		return guestID;
	}
	public void setGuestID(Integer guestID) {
		this.guestID = guestID;
	}
	public List<SeatingMarketingPrefDTO> getMarketingPreference() {
		return marketingPreference;
	}
	public void setMarketingPreference(List<SeatingMarketingPrefDTO> marketingPreference) {
		this.marketingPreference = marketingPreference;
	}
	
}
