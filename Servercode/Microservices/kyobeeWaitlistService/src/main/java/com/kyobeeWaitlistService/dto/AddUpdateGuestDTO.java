package com.kyobeeWaitlistService.dto;

public class AddUpdateGuestDTO {

	private Integer nowServingGuestId;
	private Integer nextToNotifyGuestId;
	private String op;
	private Integer totalWaitTime;
	private Integer totalGuestWaiting;
	private Integer orgId;
	private Integer addedGuestId;
	private String guestUUID;
	private String tinyURL;
	private Integer guestRank;
	private LanguageMasterDTO languagePref;
	private String clientBase;
	
	public Integer getNowServingGuestId() {
		return nowServingGuestId;
	}
	public void setNowServingGuestId(Integer nowServingGuestId) {
		this.nowServingGuestId = nowServingGuestId;
	}
	public Integer getNextToNotifyGuestId() {
		return nextToNotifyGuestId;
	}
	public void setNextToNotifyGuestId(Integer nextToNotifyGuestId) {
		this.nextToNotifyGuestId = nextToNotifyGuestId;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public Integer getTotalWaitTime() {
		return totalWaitTime;
	}
	public void setTotalWaitTime(Integer totalWaitTime) {
		this.totalWaitTime = totalWaitTime;
	}
	public Integer getTotalGuestWaiting() {
		return totalGuestWaiting;
	}
	public void setTotalGuestWaiting(Integer totalPartiesWaiting) {
		this.totalGuestWaiting = totalPartiesWaiting;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getAddedGuestId() {
		return addedGuestId;
	}
	public void setAddedGuestId(Integer addedGuestId) {
		this.addedGuestId = addedGuestId;
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
	public Integer getGuestRank() {
		return guestRank;
	}
	public void setGuestRank(Integer guestRank) {
		this.guestRank = guestRank;
	}
	public LanguageMasterDTO getLanguagePref() {
		return languagePref;
	}
	public void setLanguagePref(LanguageMasterDTO languagePref) {
		this.languagePref = languagePref;
	}
	public String getClientBase() {
		return clientBase;
	}
	public void setClientBase(String clientBase) {
		this.clientBase = clientBase;
	}
	
	
}
