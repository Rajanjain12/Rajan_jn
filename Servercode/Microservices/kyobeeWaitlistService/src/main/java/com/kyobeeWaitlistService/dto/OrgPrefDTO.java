package com.kyobeeWaitlistService.dto;

import java.util.List;

public class OrgPrefDTO {

	private List<SeatingMarketingPrefDTO> seatingPreference;
	private List<SeatingMarketingPrefDTO> marketingPreference;
   
	
	public List<SeatingMarketingPrefDTO> getSeatingPreference() {
		return seatingPreference;
	}

	public void setSeatingPreference(List<SeatingMarketingPrefDTO> seatingPreference) {
		this.seatingPreference = seatingPreference;
	}

	public List<SeatingMarketingPrefDTO> getMarketingPreference() {
		return marketingPreference;
	}

	public void setMarketingPreference(List<SeatingMarketingPrefDTO> marketingPreference) {
		this.marketingPreference = marketingPreference;
	}

}
