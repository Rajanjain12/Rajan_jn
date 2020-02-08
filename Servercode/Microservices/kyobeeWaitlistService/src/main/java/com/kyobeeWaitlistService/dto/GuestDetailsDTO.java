package com.kyobeeWaitlistService.dto;

import java.util.Date;

public class GuestDetailsDTO {
	
	private Integer GuestID;
	private Integer organizationID;
	private String name;
	private String uuid;
	private String noOfPeople;
	private Integer noOfChildren;
	private Integer noOfAdults;
	private Integer noOfInfants;
	private Integer partyType;
	private Integer calloutCount;
	private String deviceId;
	private String deviceType;
	private String sms;
	private String email;
	private String prefType;
	private Byte optin;
	private Integer rank;
	private String status;
	private Date checkinTime;
	private Date seatedTime;
	private Date createdTime;
	private Date updatedTime;
	private Integer incompleteParty;
	private Integer languagePrefID;
    private String seatingPreference;
    private String marketingPreference;
    private String note;
	
	public Integer getGuestID() {
		return GuestID;
	}
	public void setGuestID(Integer guestID) {
		GuestID = guestID;
	}
	public Integer getOrganizationID() {
		return organizationID;
	}
	public void setOrganizationID(Integer organizationID) {
		this.organizationID = organizationID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getNoOfPeople() {
		return noOfPeople;
	}
	public void setNoOfPeople(String noOfPeople) {
		this.noOfPeople = noOfPeople;
	}
	public Integer getNoOfChildren() {
		return noOfChildren;
	}
	public void setNoOfChildren(Integer noOfChildren) {
		this.noOfChildren = noOfChildren;
	}
	public Integer getNoOfAdults() {
		return noOfAdults;
	}
	public void setNoOfAdults(Integer noOfAdults) {
		this.noOfAdults = noOfAdults;
	}
	public Integer getNoOfInfants() {
		return noOfInfants;
	}
	public void setNoOfInfants(Integer noOfInfants) {
		this.noOfInfants = noOfInfants;
	}
	public Integer getPartyType() {
		return partyType;
	}
	public void setPartyType(Integer partyType) {
		this.partyType = partyType;
	}
	public Integer getCalloutCount() {
		return calloutCount;
	}
	public void setCalloutCount(Integer calloutCount) {
		this.calloutCount = calloutCount;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPrefType() {
		return prefType;
	}
	public void setPrefType(String prefType) {
		this.prefType = prefType;
	}
	public Byte getOptin() {
		return optin;
	}
	public void setOptin(Byte optin) {
		this.optin = optin;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCheckinTime() {
		return checkinTime;
	}
	public void setCheckinTime(Date checkinTime) {
		this.checkinTime = checkinTime;
	}
	public Date getSeatedTime() {
		return seatedTime;
	}
	public void setSeatedTime(Date seatedTime) {
		this.seatedTime = seatedTime;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public Integer getIncompleteParty() {
		return incompleteParty;
	}
	public void setIncompleteParty(Integer incompleteParty) {
		this.incompleteParty = incompleteParty;
	}
	public Integer getLanguagePrefID() {
		return languagePrefID;
	}
	public void setLanguagePrefID(Integer languagePrefID) {
		this.languagePrefID = languagePrefID;
	}
	public String getSeatingPreference() {
		return seatingPreference;
	}
	public void setSeatingPreference(String seatingPreference) {
		this.seatingPreference = seatingPreference;
	}
	public String getMarketingPreference() {
		return marketingPreference;
	}
	public void setMarketingPreference(String marketingPreference) {
		this.marketingPreference = marketingPreference;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
    
    

}
