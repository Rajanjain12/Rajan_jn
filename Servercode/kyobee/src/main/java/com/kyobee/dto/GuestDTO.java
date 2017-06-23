package com.kyobee.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.kyobee.util.common.RsntDateSerializer;



@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class GuestDTO implements Serializable{
	
	private static final long serialVersionUID = -2197383543076344525L;
	
	private Long guestID;
	private Long organizationID;
	private String name;
	private String note;
	private String uuid;
	private Long noOfChildren;  //changes by krupali, line 28 to 33 and line(15/06/2017)
	private Long noOfAdults;
	private Long noOfInfants;
	private Long noOfPeople;
	private int quoteTime;
	private int partyType;
	private String email;
	private String sms;
	private String status;
	private Long rank;
	private String prefType;
	private List<GuestPreferencesDTO> guestPreferences;
	private boolean optin;
	private Long calloutCount;
	private Date checkinTime;
	private Date seatedTime;
	private Date createdTime;
	private Date updatedTime;
	private Long incompleteParty;
	private String seatingPreference;
	private String deviceType;
	private String deviceId;

	
	@XmlAttribute
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	@XmlAttribute
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@XmlAttribute
	public Long getIncompleteParty() {
		return incompleteParty;
	}
	public void setIncompleteParty(Long incompleteParty) {
		this.incompleteParty = incompleteParty;
	}
	@XmlAttribute
	public Long getGuestID() {
		return guestID;
	}
	public void setGuestID(Long guestID) {
		this.guestID = guestID;
	}
	@XmlAttribute
	public Long getOrganizationID() {
		return organizationID;
	}
	public void setOrganizationID(Long organizationID) {
		this.organizationID = organizationID;
	}
	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlAttribute
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@XmlAttribute						//changes by krupali, line 102 to line 122 and line 130 to 135 (15/06/2017)
	public Long getNoOfChildren() {				
		return noOfChildren;
	}
	public void setNoOfChildren(Long noOfChildren) {	
		this.noOfChildren = noOfChildren;
	}
	@XmlAttribute
	public Long getNoOfAdults() {
		return noOfAdults;
	}
	public void setNoOfAdults(Long noOfAdults) {
		this.noOfAdults = noOfAdults;
	}
	@XmlAttribute
	public Long getNoOfInfants() {
		return noOfInfants;
	}
	public void setNoOfInfants(Long noOfInfants) {
		this.noOfInfants = noOfInfants;
	}
	@XmlAttribute
	public Long getNoOfPeople() {
		return noOfPeople;
	}
	public void setNoOfPeople(Long noOfPeople) {
		this.noOfPeople = noOfPeople;
	}
	@XmlAttribute
	public int getQuoteTime() {
		return quoteTime;
	}
	public void setQuoteTime(int quoteTime) {
		this.quoteTime = quoteTime;
	}
	@XmlAttribute
	public int getPartyType() {
		return partyType;
	}
	public void setPartyType(int partyType) {
		this.partyType = partyType;
	}
	@XmlAttribute
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@XmlAttribute
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}
	@XmlAttribute
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@XmlAttribute
	public String getPrefType() {
		return prefType;
	}
	public void setPrefType(String prefType) {
		this.prefType = prefType;
	}
	@XmlAttribute
	public boolean isOptin() {
		return optin;
	}
	public void setOptin(boolean optin) {
		this.optin = optin;
	}
	@XmlAttribute
	public Date getCheckinTime() {
		return checkinTime;
	}
	 @JsonDeserialize(using = RsntDateSerializer.class)
	public void setCheckinTime(Date checkinTime) {
		this.checkinTime = checkinTime;
	}
	@XmlAttribute
	public Date getSeatedTime() {
		return seatedTime;
	}
	 @JsonDeserialize(using = RsntDateSerializer.class)
	public void setSeatedTime(Date seatedTime) {
		this.seatedTime = seatedTime;
	}
	@XmlAttribute
	public Date getCreatedTime() {
		return createdTime;
	}
	 @JsonDeserialize(using = RsntDateSerializer.class)
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	@XmlAttribute
	public Date getUpdatedTime() {
		return updatedTime;
	}
	 @JsonDeserialize(using = RsntDateSerializer.class)
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public Long getRank() {
		return rank;
	}
	public void setRank(Long rank) {
		this.rank = rank;
	}
	
	@XmlAttribute
	public Long getCalloutCount() {
		return calloutCount;
	}

	public void setCalloutCount(Long calloutCount) {
		this.calloutCount = calloutCount;
	}
	@XmlAttribute
	public List<GuestPreferencesDTO> getGuestPreferences() {
		return guestPreferences;
	}
	public void setGuestPreferences(List<GuestPreferencesDTO> guestPreferences) {
		this.guestPreferences = guestPreferences;
	}
	
	@XmlAttribute
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	@XmlAttribute
	public String getSeatingPreference() {
		return seatingPreference;
	}

	public void setSeatingPreference(String seatingPreference) {
		this.seatingPreference = seatingPreference;
	}
	
}


