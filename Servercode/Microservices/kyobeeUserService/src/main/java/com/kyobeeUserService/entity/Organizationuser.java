package com.kyobeeUserService.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class Organizationuser {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OrganizationUserID")
	private int organizationUserID;

	@Temporal(TemporalType.DATE)
	@Column(name="CreatedAt")
	private Date createdAt;

	@Column(name="CreatedBy")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="ModifiedAt")
	private Date modifiedAt;

	@Column(name="ModifiedBy")
	private String modifiedBy;

	//bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name="OrganizationID")
	private Organization organization1;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="UserID")
	private User user1;

	//bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name="OrganizationID")
	private Organization organization2;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="UserID")
	private User user2;

	public Organizationuser() {
	}

	public int getOrganizationUserID() {
		return this.organizationUserID;
	}

	public void setOrganizationUserID(int organizationUserID) {
		this.organizationUserID = organizationUserID;
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

	public Organization getOrganization1() {
		return this.organization1;
	}

	public void setOrganization1(Organization organization1) {
		this.organization1 = organization1;
	}

	public User getUser1() {
		return this.user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public Organization getOrganization2() {
		return this.organization2;
	}

	public void setOrganization2(Organization organization2) {
		this.organization2 = organization2;
	}

	public User getUser2() {
		return this.user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}


}
