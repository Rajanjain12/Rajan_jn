package com.kyobeeWaitlistService.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "GUEST")
public class Guest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "GuestID")
	private Integer guestID;

	@Column(name = "OrganizationID")
	private Integer organizationID;

	@Column(name = "Name")
	private String name;

	@Column(name = "Uuid")
	private String uuid;

	@Column(name = "NoOfAdults")
	private Integer noOfAdults;

	@Column(name = "NoOfChildren")
	private Integer noOfChildren;

	@Column(name = "NoOfInfants")
	private Integer noOfInfants;

	@Column(name = "NoOfPeople")
	private Integer noOfPeople;

	@Column(name = "QuoteTime")
	private Integer quoteTime;

	@Column(name = "PartyType")
	private Integer partyType;

	@Column(name = "DeviceType")
	private String deviceType;

	@Column(name = "DeviceId")
	private String deviceId;

	@Column(name = "ContactNo")
	private String contactNo;

	@Column(name = "Email")
	private String email;

	@Column(name = "PrefType")
	private String prefType;

	// bi-directional many-to-one association to Langmaster
	@ManyToOne
	@JoinColumn(name = "LanguagePrefID")
	private LangMaster langmaster;

	@Column(name = "Optin")
	private Byte optin;

	@Column(name = "Rank")
	private Integer rank;

	@Column(name = "Status")
	private String status;

	@Column(name = "SeatingPreference")
	private String seatingPreference;

	@Column(name = "RecvLeveltwo")
	private Byte recvLeveltwo;

	@Column(name = "CalloutCount")
	private Integer calloutCount;

	@Column(name = "Note")
	private String note;

	@Column(name = "IncompleteParty")
	private Integer incompleteParty;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ResetTime")
	private Date resetTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CheckinTime")
	private Date checkinTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SeatedTime")
	private Date seatedTime;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedAt")
	private Date createdAt;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ModifiedAt")
	private Date modifiedAt;

	public Guest() {
		// constructor
	}

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

	public Integer getNoOfAdults() {
		return noOfAdults;
	}

	public void setNoOfAdults(Integer noOfAdults) {
		this.noOfAdults = noOfAdults;
	}

	public Integer getNoOfChildren() {
		return noOfChildren;
	}

	public void setNoOfChildren(Integer noOfChildren) {
		this.noOfChildren = noOfChildren;
	}

	public Integer getNoOfInfants() {
		return noOfInfants;
	}

	public void setNoOfInfants(Integer noOfInfants) {
		this.noOfInfants = noOfInfants;
	}

	public Integer getNoOfPeople() {
		return noOfPeople;
	}

	public void setNoOfPeople(Integer noOfPeople) {
		this.noOfPeople = noOfPeople;
	}

	public Integer getQuoteTime() {
		return quoteTime;
	}

	public void setQuoteTime(Integer quoteTime) {
		this.quoteTime = quoteTime;
	}

	public Integer getPartyType() {
		return partyType;
	}

	public void setPartyType(Integer partyType) {
		this.partyType = partyType;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
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

	public LangMaster getLangmaster() {
		return langmaster;
	}

	public void setLangmaster(LangMaster langmaster) {
		this.langmaster = langmaster;
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

	public String getSeatingPreference() {
		return seatingPreference;
	}

	public void setSeatingPreference(String seatingPreference) {
		this.seatingPreference = seatingPreference;
	}

	public Byte getRecvLeveltwo() {
		return recvLeveltwo;
	}

	public void setRecvLeveltwo(Byte recvLeveltwo) {
		this.recvLeveltwo = recvLeveltwo;
	}

	public Integer getCalloutCount() {
		return calloutCount;
	}

	public void setCalloutCount(Integer calloutCount) {
		this.calloutCount = calloutCount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getIncompleteParty() {
		return incompleteParty;
	}

	public void setIncompleteParty(Integer incompleteParty) {
		this.incompleteParty = incompleteParty;
	}

	public Date getResetTime() {
		return resetTime;
	}

	public void setResetTime(Date resetTime) {
		this.resetTime = resetTime;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

}
