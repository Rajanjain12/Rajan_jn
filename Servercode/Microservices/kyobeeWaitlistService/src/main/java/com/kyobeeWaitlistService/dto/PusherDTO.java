package com.kyobeeWaitlistService.dto;

import com.kyobeeWaitlistService.entity.LangMaster;

public class PusherDTO {
	
	private Integer totalPartiesWaiting;
	private Integer nextToNotifyGuestId;
	private String op;
	private Integer addedGuestId;
	private Integer totalWaitTime;
	private Integer nowServingGuestId;
	private Integer guestRank;
	private String guestUUID;
	private Integer orgId;
	private String tinyURL;
	private Integer partyType;
	private String from;
	private String name;
	private LangMaster languagePrefID;
	private Integer perPartyWaitTime;
	
	public Integer getTotalPartiesWaiting() {
		return totalPartiesWaiting;
	}
	public void setTotalPartiesWaiting(Integer totalPartiesWaiting) {
		this.totalPartiesWaiting = totalPartiesWaiting;
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
	public Integer getAddedGuestId() {
		return addedGuestId;
	}
	public void setAddedGuestId(Integer addedGuestId) {
		this.addedGuestId = addedGuestId;
	}
	public Integer getTotalWaitTime() {
		return totalWaitTime;
	}
	public void setTotalWaitTime(Integer totalWaitTime) {
		this.totalWaitTime = totalWaitTime;
	}
	public Integer getNowServingGuestId() {
		return nowServingGuestId;
	}
	public void setNowServingGuestId(Integer nowServingGuestId) {
		this.nowServingGuestId = nowServingGuestId;
	}
	public Integer getGuestRank() {
		return guestRank;
	}
	public void setGuestRank(Integer guestRank) {
		this.guestRank = guestRank;
	}
	public String getGuestUUID() {
		return guestUUID;
	}
	public void setGuestUUID(String guestUUID) {
		this.guestUUID = guestUUID;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getTinyURL() {
		return tinyURL;
	}
	public void setTinyURL(String tinyURL) {
		this.tinyURL = tinyURL;
	}
	public Integer getPartyType() {
		return partyType;
	}
	public void setPartyType(Integer partyType) {
		this.partyType = partyType;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LangMaster getLanguagePrefID() {
		return languagePrefID;
	}
	public void setLanguagePrefID(LangMaster languagePrefID) {
		this.languagePrefID = languagePrefID;
	}
	public Integer getPerPartyWaitTime() {
		return perPartyWaitTime;
	}
	public void setPerPartyWaitTime(Integer perPartyWaitTime) {
		this.perPartyWaitTime = perPartyWaitTime;
	}
	
}
