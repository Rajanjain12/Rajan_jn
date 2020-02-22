package com.kyobeeWaitlistService.dto;

public class AddUpdateGuestDTO {
	
	private WaitlistMetrics waitlistMetrics;
	private String op;
	private Integer orgId;
	private String guestUUID;
	private String tinyURL;
	private LanguageMasterDTO languagePref;
	
	public WaitlistMetrics getWaitlistMetrics() {
		return waitlistMetrics;
	}
	public void setWaitlistMetrics(WaitlistMetrics waitlistMetrics) {
		this.waitlistMetrics = waitlistMetrics;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getGuestUUID() {
		return guestUUID;
	}
	public void setGuestUUID(String guestUUID) {
		this.guestUUID = guestUUID;
	}
	public String getTinyURL() {
		return tinyURL;
	}
	public void setTinyURL(String tinyURL) {
		this.tinyURL = tinyURL;
	}
	public LanguageMasterDTO getLanguagePref() {
		return languagePref;
	}
	public void setLanguagePref(LanguageMasterDTO languagePref) {
		this.languagePref = languagePref;
	}
	
}
