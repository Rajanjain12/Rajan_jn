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
@Table(name="GUEST")
public class Guest implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	private Integer guestID;

	private Integer calloutCount;

	@Temporal(TemporalType.TIMESTAMP)
	private Date checkinTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	private String deviceId;

	private String deviceType;

	private String email;

	private Integer incompleteParty;

	private String marketingPreference;

	private String name;

	private Integer noOfAdults;

	private Integer noOfChildren;

	private Integer noOfInfants;

	private Integer noOfPeople;

	private String note;

	private Byte optin;

	@Column(name="OrganizationID")
	private Integer organizationID;

	private Integer partyType;

	private String prefType;

	private Integer quoteTime;

	private Integer rank;

	private Byte recvLeveltwo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date resetTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date seatedTime;

	private String seatingPreference;

	private String contactNo;

	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;

	private String uuid;

	//bi-directional many-to-one association to Langmaster
	@ManyToOne
	@JoinColumn(name="languagePrefID")
	private LangMaster langmaster;

	public Guest() {
		//constructor 
	}

	public Integer getGuestID() {
		return this.guestID;
	}

	public void setGuestID(Integer guestID) {
		this.guestID = guestID;
	}

	public Integer getCalloutCount() {
		return this.calloutCount;
	}

	public void setCalloutCount(Integer calloutCount) {
		this.calloutCount = calloutCount;
	}

	public Date getCheckinTime() {
		return this.checkinTime;
	}

	public void setCheckinTime(Date checkinTime) {
		this.checkinTime = checkinTime;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return this.deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getIncompleteParty() {
		return this.incompleteParty;
	}

	public void setIncompleteParty(Integer incompleteParty) {
		this.incompleteParty = incompleteParty;
	}

	public String getMarketingPreference() {
		return this.marketingPreference;
	}

	public void setMarketingPreference(String marketingPreference) {
		this.marketingPreference = marketingPreference;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNoOfAdults() {
		return this.noOfAdults;
	}

	public void setNoOfAdults(Integer noOfAdults) {
		this.noOfAdults = noOfAdults;
	}

	public Integer getNoOfChildren() {
		return this.noOfChildren;
	}

	public void setNoOfChildren(Integer noOfChildren) {
		this.noOfChildren = noOfChildren;
	}

	public Integer getNoOfInfants() {
		return this.noOfInfants;
	}

	public void setNoOfInfants(Integer noOfInfants) {
		this.noOfInfants = noOfInfants;
	}

	public Integer getNoOfPeople() {
		return this.noOfPeople;
	}

	public void setNoOfPeople(Integer noOfPeople) {
		this.noOfPeople = noOfPeople;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Byte getOptin() {
		return this.optin;
	}

	public void setOptin(Byte optin) {
		this.optin = optin;
	}

	public Integer getOrganizationID() {
		return this.organizationID;
	}

	public void setOrganizationID(Integer organizationID) {
		this.organizationID = organizationID;
	}

	public Integer getPartyType() {
		return this.partyType;
	}

	public void setPartyType(Integer partyType) {
		this.partyType = partyType;
	}

	public String getPrefType() {
		return this.prefType;
	}

	public void setPrefType(String prefType) {
		this.prefType = prefType;
	}

	public Integer getQuoteTime() {
		return this.quoteTime;
	}

	public void setQuoteTime(Integer quoteTime) {
		this.quoteTime = quoteTime;
	}

	public Integer getRank() {
		return this.rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Byte getRecvLeveltwo() {
		return this.recvLeveltwo;
	}

	public void setRecvLeveltwo(Byte recvLeveltwo) {
		this.recvLeveltwo = recvLeveltwo;
	}

	public Date getResetTime() {
		return this.resetTime;
	}

	public void setResetTime(Date resetTime) {
		this.resetTime = resetTime;
	}

	public Date getSeatedTime() {
		return this.seatedTime;
	}

	public void setSeatedTime(Date seatedTime) {
		this.seatedTime = seatedTime;
	}

	public String getSeatingPreference() {
		return this.seatingPreference;
	}

	public void setSeatingPreference(String seatingPreference) {
		this.seatingPreference = seatingPreference;
	}
	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public LangMaster getLangmaster() {
		return this.langmaster;
	}

	public void setLangmaster(LangMaster langmaster) {
		this.langmaster = langmaster;
	}
}
