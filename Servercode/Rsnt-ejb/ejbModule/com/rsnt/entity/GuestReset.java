package com.rsnt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.Type;
import org.jboss.seam.annotations.Name;

@Entity
@Table(name="GUESTRESET")
@Name("GuestReset")
public class GuestReset implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="guestResetID")
	private Long guestResetID;
	
	@Column(name="OrganizationID")
	private Long OrganizationID;
	
	@Column(name="name")
	private String name;
	
	@Column(name="note")
	private String note;
	
	@Column(name="uuid")
	private String uuid;
	
	@Column(name="noOfPeople")
	private Long noOfPeople;
	
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
	
	public Long getIncompleteParty() {
		return incompleteParty;
	}

	public void setIncompleteParty(Long incompleteParty) {
		this.incompleteParty = incompleteParty;
	}

	public Long getGuestResetID() {
		return guestResetID;
	}

	public void setGuestResetID(Long guestResetID) {
		this.guestResetID = guestResetID;
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

	public Long getNoOfPeople() {
		return noOfPeople;
	}

	public void setNoOfPeople(Long noOfPeople) {
		this.noOfPeople = noOfPeople;
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
}
