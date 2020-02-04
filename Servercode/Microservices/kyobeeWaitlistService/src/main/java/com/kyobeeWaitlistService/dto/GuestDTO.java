package com.kyobeeWaitlistService.dto;

import java.util.Date;
import java.util.List;

public class GuestDTO {

	private Integer guestID;
	private Integer organizationID;
	private String name;
	private String uuid;
	private String noOfPeople;
	private Integer partyType;
	private Integer calloutCount;
	private String deviceId;
	private String deviceType;
	private String phoneNumber;
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
	private LanguageMasterDTO langguagePref;
    private List<SeatingMarketingPrefDTO> seatingPreference;
    private List<SeatingMarketingPrefDTO> marketingPreference;
    
	public Integer getGuestID() {
		return guestID;
	}
	public void setGuestID(Integer guestID) {
		this.guestID = guestID;
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
	public LanguageMasterDTO getLangguagePref() {
		return langguagePref;
	}
	public void setLangguagePref(LanguageMasterDTO langguagePref) {
		this.langguagePref = langguagePref;
	}
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
