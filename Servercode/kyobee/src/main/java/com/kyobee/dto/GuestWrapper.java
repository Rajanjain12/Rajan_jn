/**
 * 
 */
package com.kyobee.dto;

import java.util.Date;
import java.util.List;

/**
 * @author Vruddhi
 * Used for checkinUsers api in waitlist
 */
public class GuestWrapper {

	private Integer row;
	private Long guestID;
	private Long organizationID;
	private String name;
	private String note;
	private String uuid;
	private Long noOfPeople;
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
	private Integer waitTime;
	
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	public Long getGuestID() {
		return guestID;
	}
	public void setGuestID(Long guestID) {
		this.guestID = guestID;
	}
	public Long getOrganizationID() {
		return organizationID;
	}
	public void setOrganizationID(Long organizationID) {
		this.organizationID = organizationID;
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
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Long getNoOfPeople() {
		return noOfPeople;
	}
	public void setNoOfPeople(Long noOfPeople) {
		this.noOfPeople = noOfPeople;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getRank() {
		return rank;
	}
	public void setRank(Long rank) {
		this.rank = rank;
	}
	public String getPrefType() {
		return prefType;
	}
	public void setPrefType(String prefType) {
		this.prefType = prefType;
	}
	public List<GuestPreferencesDTO> getGuestPreferences() {
		return guestPreferences;
	}
	public void setGuestPreferences(List<GuestPreferencesDTO> guestPreferences) {
		this.guestPreferences = guestPreferences;
	}
	public boolean isOptin() {
		return optin;
	}
	public void setOptin(boolean optin) {
		this.optin = optin;
	}
	public Long getCalloutCount() {
		return calloutCount;
	}
	public void setCalloutCount(Long calloutCount) {
		this.calloutCount = calloutCount;
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
	public Long getIncompleteParty() {
		return incompleteParty;
	}
	public void setIncompleteParty(Long incompleteParty) {
		this.incompleteParty = incompleteParty;
	}
	public String getSeatingPreference() {
		return seatingPreference;
	}
	public void setSeatingPreference(String seatingPreference) {
		this.seatingPreference = seatingPreference;
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
	public Integer getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(Integer waitTime) {
		this.waitTime = waitTime;
	}
	
	
	
}
