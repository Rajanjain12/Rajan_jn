package com.kyobeeWaitlistService.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CUSTOMER")
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CustomerID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer customerID;

	@Column(name = "CustomerName")
	private String customerName;

	// bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name = "CorporateOrgId")
	private Organization organization;

	@Temporal(TemporalType.DATE)
	@Column(name = "TrialPeriodStartDate")
	private Date trialPeriodStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "TrialPeriodEndDate")
	private Date trialPeriodEndDate;

	@Column(name = "Active")
	private Byte active;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CreatedAt")
	private Date createdAt;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "ModifiedAt")
	private Date modifiedAt;

	// bi-directional many-to-one association to Organization
	@OneToMany(mappedBy = "customer")
	private List<Organization> organizations;

	// bi-directional many-to-one association to Organizationcarddetail
	@OneToMany(mappedBy = "customer")
	private List<OrganizationCardDetail> organizationCardDetails;

	// bi-directional many-to-one association to Organizationuser
	@OneToMany(mappedBy = "customer")
	private List<OrganizationUser> organizationUsers;

	public Integer getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Date getTrialPeriodStartDate() {
		return trialPeriodStartDate;
	}

	public void setTrialPeriodStartDate(Date trialPeriodStartDate) {
		this.trialPeriodStartDate = trialPeriodStartDate;
	}

	public Date getTrialPeriodEndDate() {
		return trialPeriodEndDate;
	}

	public void setTrialPeriodEndDate(Date trialPeriodEndDate) {
		this.trialPeriodEndDate = trialPeriodEndDate;
	}

	public Byte getActive() {
		return active;
	}

	public void setActive(Byte active) {
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

	public List<Organization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}

	public List<OrganizationCardDetail> getOrganizationcarddetails() {
		return organizationCardDetails;
	}

	public void setOrganizationcarddetails(List<OrganizationCardDetail> organizationCardDetails) {
		this.organizationCardDetails = organizationCardDetails;
	}

	public List<OrganizationUser> getOrganizationusers() {
		return organizationUsers;
	}

	public void setOrganizationusers(List<OrganizationUser> organizationUsers) {
		this.organizationUsers = organizationUsers;
	}

}
