package com.kyobee.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.Type;

import com.kyobee.dto.GuestMarketingPreference;
import com.kyobee.dto.LanguageMasterDTO;

@Entity
@Table(name="GUEST")
public class Guest implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="guestID")
	private Long guestID;
	
	@Column(name="OrganizationID")
	private Long OrganizationID;
	
	@Column(name="name")
	private String name;
	
	@Column(name="note")
	private String note;
	
	@Column(name="uuid")
	private String uuid;
	
	@Column(name="noOfChildren")	//changes by krupali, line 39 to line 46 and line 51 to 55 (15/06/2017)
	private Long noOfChildren;
	
	@Column(name="noOfAdults")
	private Long noOfAdults;
	
	@Column(name="noOfInfants")
	private Long noOfInfants;
	
	@Column(name="noOfPeople")
	private Long noOfPeople;
	
	@Column(name="quoteTime")
	private Integer quoteTime;

	@Column(name="partyType")
	private Integer partyType;

	@Column(name="sms")
	private String sms;
	
	@Column(name="email")
	private String email;
	
	@Column(columnDefinition = "TINYINT",name="optin", nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean optin;
	
	@Column(name="rank")
	private Long rank;
	
	@Column(name="status")
	private String status;
	
	@Transient
	@JsonManagedReference
	private List<GuestPreferences> guestPreferences;
	
	@Transient
	@JsonManagedReference
	private List<GuestMarketingPreference> guestMarketingPreferences;
	
	@Column(name="checkinTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkinTime;
	
	@Column(name="seatedTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date seatedTime;
	
	@Column(name="createdTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	
	@Column(name="updatedTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;
	
	@Column(name="prefType")
	private String prefType;
	
	@Column(name="resetTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date resetTime;
	
	@Column(name="calloutCount")
	private Long calloutCount;
	
	@Column(name="incompleteParty")
	private Long incompleteParty;
	
	@Column(name="seatingPreference")
	private String seatingPreference;
	
	@Column(name="marketingPreference")
	private String marketingPreference;// change by sunny 2018-07-04..
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="languagePrefID", nullable = false)
	private LangMaster languagePrefID;
	
	@Column(name="deviceID")
	private String deviceId;

	@Column(name="deviceType")
	private String deviceType;
	
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMarketingPreference() {
		return marketingPreference;
	}

	public void setMarketingPreference(String marketingPreference) {
		this.marketingPreference = marketingPreference;
	}
	
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	public Long getIncompleteParty() {
		return incompleteParty;
	}

	public void setIncompleteParty(Long incompleteParty) {
		this.incompleteParty = incompleteParty;
	}

	public Long getGuestID() {
		return guestID;
	}

	public void setGuestID(Long guestID) {
		this.guestID = guestID;
	}

	public Long getOrganizationID() {
		return OrganizationID;
	}

	public void setOrganizationID(Long organizationID) {
		OrganizationID = organizationID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public Long getNoOfChildren() {						//change by krupali, line 168 to line 190 and line 203 to 217 (14/06/2017)
		return noOfChildren;
	}

	public void setNoOfChildren(Long noOfChildren) {
		this.noOfChildren = noOfChildren;
	}

	public Long getNoOfAdults() {
		return noOfAdults;
	}

	public void setNoOfAdults(Long noOfAdults) {
		this.noOfAdults = noOfAdults;
	}

	public Long getNoOfInfants() {
		return noOfInfants;
	}

	public void setNoOfInfants(Long noOfInfants) {
		this.noOfInfants = noOfInfants;
	}

	public Long getNoOfPeople() {
		return noOfPeople;
	}

	public void setNoOfPeople(Long noOfPeople) {
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

	public boolean isOptin() {
		return optin;
	}

	public void setOptin(boolean optin) {
		this.optin = optin;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPrefType() {
		return prefType;
	}

	public void setPrefType(String prefType) {
		this.prefType = prefType;
	}

	public Date getResetTime() {
		return resetTime;
	}

	public void setResetTime(Date resetTime) {
		this.resetTime = resetTime;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public Long getCalloutCount() {
		return calloutCount;
	}

	public void setCalloutCount(Long calloutCount) {
		this.calloutCount = calloutCount;
	}

	public List<GuestPreferences> getGuestPreferences() {
		return guestPreferences;
	}

	public void setGuestPreferences(List<GuestPreferences> guestPreferences) {
		this.guestPreferences = guestPreferences;
	}

	public String getSeatingPreference() {
		return seatingPreference;
	}

	public void setSeatingPreference(String seatingPreference) {
		this.seatingPreference = seatingPreference;
	}

	public LangMaster getLanguagePrefID() {
		return languagePrefID;
	}

	public void setLanguagePrefID(LangMaster languagePrefID) {
		this.languagePrefID = languagePrefID;
	}

	public List<GuestMarketingPreference> getGuestMarketingPreferences() {
		return guestMarketingPreferences;
	}

	public void setGuestMarketingPreferences(List<GuestMarketingPreference> guestMarketingPreferences) {
		this.guestMarketingPreferences = guestMarketingPreferences;
	}

	
}
