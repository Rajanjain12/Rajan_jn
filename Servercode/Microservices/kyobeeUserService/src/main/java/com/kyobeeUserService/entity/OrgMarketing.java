package com.kyobeeUserService.entity;

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
@Table(name="ORGMARKETING")
public class OrgMarketing implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OrgMarketPrefID")
	private Integer orgMarketPrefID;

	@Column(name="Active")
	private Integer active;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreatedAt")
	private Date createdAt;

	@Column(name="CreatedBy")
	private String createdBy;

	@Column(name="Facebook")
	private Integer facebook;

	@Column(name="Googleplus")
	private Integer googleplus;

	@Column(name="GuestID")
	private Integer guestID;

	@Column(name="Instagram")
	private Integer instagram;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ModifiedAt")
	private Date modifiedAt;

	@Column(name="ModifiedBy")
	private String modifiedBy;

	@Column(name="WordofMouth")
	private Integer wordofMouth;

	//bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name="OrgID")
	private Organization organization;

	public OrgMarketing() {
	}

	public Integer getOrgMarketPrefID() {
		return this.orgMarketPrefID;
	}

	public void setOrgMarketPrefID(Integer orgMarketPrefID) {
		this.orgMarketPrefID = orgMarketPrefID;
	}

	public Integer getActive() {
		return this.active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getFacebook() {
		return this.facebook;
	}

	public void setFacebook(Integer facebook) {
		this.facebook = facebook;
	}

	public Integer getGoogleplus() {
		return this.googleplus;
	}

	public void setGoogleplus(Integer googleplus) {
		this.googleplus = googleplus;
	}

	public Integer getGuestID() {
		return this.guestID;
	}

	public void setGuestID(Integer guestID) {
		this.guestID = guestID;
	}

	public Integer getInstagram() {
		return this.instagram;
	}

	public void setInstagram(Integer instagram) {
		this.instagram = instagram;
	}

	public Date getModifiedAt() {
		return this.modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getWordofMouth() {
		return this.wordofMouth;
	}

	public void setWordofMouth(Integer wordofMouth) {
		this.wordofMouth = wordofMouth;
	}

	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
}
