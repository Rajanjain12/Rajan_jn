package com.kyobee.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

// created by sunny for marketing preference 2018-07-05

public class GuestMarketingPreferenceV2 implements Serializable{
	//private Long guestMarketPrefId;
	private Long guestMarketPrefValueId;
	private String guestMarketPrefValue;
	private boolean selected;
	private String guestMarketPrefKey;
	
	
	
	
	public String getGuestMarketPrefKey() {
		return guestMarketPrefKey;
	}
	public void setGuestMarketPrefKey(String guestMarketPrefKey) {
		this.guestMarketPrefKey = guestMarketPrefKey;
	}
	/*@XmlAttribute
	public Long getGuestMarketPrefId() {
		return guestMarketPrefId;
	}
	public void setGuestMarketPrefId(Long guestMarketPrefId) {
		this.guestMarketPrefId = guestMarketPrefId;
	}*/
	@XmlAttribute
	public Long getGuestMarketPrefValueId() {
		return guestMarketPrefValueId;
	}
	public void setGuestMarketPrefValueId(Long guestMarketPrefValueId) {
		this.guestMarketPrefValueId = guestMarketPrefValueId;
	}
	@XmlAttribute
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
