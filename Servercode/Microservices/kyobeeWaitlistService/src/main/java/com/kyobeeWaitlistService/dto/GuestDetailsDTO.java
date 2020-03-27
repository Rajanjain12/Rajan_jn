package com.kyobeeWaitlistService.dto;

import java.util.Date;

//DTO is used in Mobile fetch detail API
public class GuestDetailsDTO {

	private Integer GuestID;
	private Integer OrganizationID;
	private String Name;
	private String Note;
	private String Uuid;
	private Integer NoOfAdults;
	private Integer NoOfChildren;
	private Integer NoOfInfants;
	private Integer NoOfPeople;
	private String Email;
	private String ContactNo;
	private String Status;
	private Integer Rank;
	private String PrefType;
	private Byte Optin;
	private Integer CalloutCount;
	private Date CheckinTime;
	private Date SeatedTime;
	private Date CreatedAt;
	private Date ModifiedAt;
	private Integer IncompleteParty;
	private String SeatingPreference;
	private String DeviceId;
	private String DeviceType;
	private Integer LanguagePrefID;
	
	

	public Integer getGuestID() {
		return GuestID;
	}

	public void setGuestID(Integer guestID) {
		GuestID = guestID;
	}

	public Integer getOrganizationID() {
		return OrganizationID;
	}

	public void setOrganizationID(Integer organizationID) {
		OrganizationID = organizationID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getUuid() {
		return Uuid;
	}

	public void setUuid(String uuid) {
		Uuid = uuid;
	}

	

	public Integer getNoOfChildren() {
		return NoOfChildren;
	}

	public void setNoOfChildren(Integer noOfChildren) {
		NoOfChildren = noOfChildren;
	}

	public Integer getNoOfAdults() {
		return NoOfAdults;
	}

	public void setNoOfAdults(Integer noOfAdults) {
		NoOfAdults = noOfAdults;
	}

	public Integer getNoOfInfants() {
		return NoOfInfants;
	}

	public void setNoOfInfants(Integer noOfInfants) {
		NoOfInfants = noOfInfants;
	}

	public Integer getCalloutCount() {
		return CalloutCount;
	}

	public void setCalloutCount(Integer calloutCount) {
		CalloutCount = calloutCount;
	}

	public String getDeviceId() {
		return DeviceId;
	}

	public void setDeviceId(String deviceId) {
		DeviceId = deviceId;
	}

	public String getDeviceType() {
		return DeviceType;
	}

	public void setDeviceType(String deviceType) {
		DeviceType = deviceType;
	}

	public String getContactNo() {
		return ContactNo;
	}

	public void setContactNo(String contactNo) {
		ContactNo = contactNo;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPrefType() {
		return PrefType;
	}

	public void setPrefType(String prefType) {
		PrefType = prefType;
	}

	public Byte getOptin() {
		return Optin;
	}

	public void setOptin(Byte optin) {
		Optin = optin;
	}

	public Integer getRank() {
		return Rank;
	}

	public void setRank(Integer rank) {
		Rank = rank;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public Date getCheckinTime() {
		return CheckinTime;
	}

	public void setCheckinTime(Date checkinTime) {
		CheckinTime = checkinTime;
	}

	public Date getSeatedTime() {
		return SeatedTime;
	}

	public void setSeatedTime(Date seatedTime) {
		SeatedTime = seatedTime;
	}

	public Date getCreatedAt() {
		return CreatedAt;
	}

	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}

	public Date getModifiedAt() {
		return ModifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		ModifiedAt = modifiedAt;
	}

	public Integer getIncompleteParty() {
		return IncompleteParty;
	}

	public void setIncompleteParty(Integer incompleteParty) {
		IncompleteParty = incompleteParty;
	}

	public Integer getLanguagePrefID() {
		return LanguagePrefID;
	}

	public void setLanguagePrefID(Integer languagePrefID) {
		LanguagePrefID = languagePrefID;
	}

	public String getSeatingPreference() {
		return SeatingPreference;
	}

	public void setSeatingPreference(String seatingPreference) {
		SeatingPreference = seatingPreference;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

}
