package com.kyobeeUserService.dto;

public class GuestMarketingPreference {

	private Long guestMarketPrefValueId;
	private String guestMarketPrefValue;
	private boolean selected;
	
	public Long getGuestMarketPrefValueId() {
		return guestMarketPrefValueId;
	}
	public void setGuestMarketPrefValueId(Long guestMarketPrefValueId) {
		this.guestMarketPrefValueId = guestMarketPrefValueId;
	}
	public String getGuestMarketPrefValue() {
		return guestMarketPrefValue;
	}
	public void setGuestMarketPrefValue(String guestMarketPrefValue) {
		this.guestMarketPrefValue = guestMarketPrefValue;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
}
