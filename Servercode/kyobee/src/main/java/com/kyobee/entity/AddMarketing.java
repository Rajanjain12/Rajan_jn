package com.kyobee.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



@Entity
@Table(name="ORGMARKETING")
public class AddMarketing {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OrgMarketPrefID")
	private int OrgMarketPrefID;
	
	@Column(name="GuestID")
	private int guestID;
	
	
	@Column(name="OrgID")
	private int orgID;
	
	
	@Column(name="Instagram")
	private int instagramStatus;
	
	@Column(name="Facebook")
	private int facebookStatus;
	
	@Column(name="Googleplus")
	private int googlePlusStatus;
	
	@Column(name="WordofMouth")
	private int wordofMouthStatus;
	
	@Column(name="Active")
	private int active;
	
	@Column(name="CreatedBy")
	private String createdBy;
	
	@Column(name="CreatedAt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name="ModifiedBy")
	private String modifiedBy;

	@Column(name="ModifiedAt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedAt;
	
	public int getOrgMarketPrefID() {
		return OrgMarketPrefID;
	}

	public void setOrgMarketPrefID(int orgMarketPrefID) {
		OrgMarketPrefID = orgMarketPrefID;
	}

	public int getGuestID() {
		return guestID;
	}

	public void setGuestID(int guestID) {
		this.guestID = guestID;
	}

	public int getOrgID() {
		return orgID;
	}

	public void setOrgID(int orgID) {
		this.orgID = orgID;
	}

	
	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
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

	
	public int getInstagramStatus() {
		return instagramStatus;
	}

	public void setInstagramStatus(int instagramStatus) {
		this.instagramStatus = instagramStatus;
	}

	public int getFacebookStatus() {
		return facebookStatus;
	}

	public void setFacebookStatus(int facebookStatus) {
		this.facebookStatus = facebookStatus;
	}

	public int getGooglePlusStatus() {
		return googlePlusStatus;
	}

	public void setGooglePlusStatus(int googlePlusStatus) {
		this.googlePlusStatus = googlePlusStatus;
	}

	public int getWordofMouthStatus() {
		return wordofMouthStatus;
	}

	public void setWordofMouthStatus(int wordofMouthStatus) {
		this.wordofMouthStatus = wordofMouthStatus;
	}



	
}
