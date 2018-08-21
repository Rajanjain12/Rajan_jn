package com.kyobee.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

public class MarketingPreferenceDTO {
	
	private int orgID;
	private int guestID;
	//private List<MarketingCategoryDTO> marketingCategoryDTO;
	private List<GuestMarketingPreference> guestMarketingPreference;
	
	
	@XmlAttribute
	public int getOrgID() {
		return orgID;
	}
	public void setOrgID(int orgID) {
		this.orgID = orgID;
	}
	@XmlAttribute
	public int getGuestID() {
		return guestID;
	}
	public void setGuestID(int guestID) {
		this.guestID = guestID;
	}
	@XmlAttribute
	public List<GuestMarketingPreference> getGuestMarketingPreference() {
		return guestMarketingPreference;
	}
	public void setGuestMarketingPreference(List<GuestMarketingPreference> guestMarketingPreference) {
		this.guestMarketingPreference = guestMarketingPreference;
	}
	
	
	/*@XmlAttribute
	public List<MarketingCategoryDTO> getMarketingCategoryDTO() {
		return marketingCategoryDTO;
	}
	public void setMarketingCategoryDTO(List<MarketingCategoryDTO> marketingCategoryDTO) {
		this.marketingCategoryDTO = marketingCategoryDTO;
	}*/
	
	
	
}
