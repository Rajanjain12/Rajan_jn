package com.kyobeeUserService.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "ORGANIZATIONSUBSCRIPTIONDETAIL")
public class OrganizationSubscriptionDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "OrganizationSubscriptionPlanID")
	private int organizationSubscriptionPlanID;

	@ManyToOne
	@JoinColumn(name = "OrganizationID")
	private Organization organization;

	// bi-directional many-to-one association to Planfeaturecharge
	@ManyToOne
	@JoinColumn(name = "PlanFeatureChargeID")
	private PlanFeatureCharge planFeatureCharge;

	@ManyToOne
	@JoinColumn(name = "FeatureID")
	private Feature feature;

	// bi-directional many-to-one association to Plan
	@ManyToOne
	@JoinColumn(name = "PlanID")
	private Plan plan;

	// bi-directional many-to-one association to Currency
	@ManyToOne
	@JoinColumn(name = "CurrencyID")
	private Currency currency;

	@ManyToOne
	@JoinColumn(name = "PlanTermID")
	private PlanTerm planTerm;

	@Column(name = "TermChargeAmt")
	private BigDecimal termChargeAmt;

	@Temporal(TemporalType.DATE)
	@Column(name = "StartDate")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "EndDate")
	private Date endDate;

	@Column(name = "SubscriptionStatus")
	private String subscriptionStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "SubscribedDate")
	private Date subscribedDate;

	@Column(name = "RenewalType")
	private String renewalType;

	@Column(name = "IsFree")
	private String isFree;

	// bi-directional many-to-one association to Organizationsubscription
	@ManyToOne
	@JoinColumn(name = "SubscriptionID")
	private OrganizationSubscription organizationSubscription;

	@Column(name = "CurrentActiveSubscription")
	private String currentActiveSubscription;

	@Column(name = "Active")
	private byte active;

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

	public int getOrganizationSubscriptionPlanID() {
		return organizationSubscriptionPlanID;
	}

	public void setOrganizationSubscriptionPlanID(int organizationSubscriptionPlanID) {
		this.organizationSubscriptionPlanID = organizationSubscriptionPlanID;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public PlanFeatureCharge getPlanFeatureCharge() {
		return planFeatureCharge;
	}

	public void setPlanFeatureCharge(PlanFeatureCharge planFeatureCharge) {
		this.planFeatureCharge = planFeatureCharge;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public PlanTerm getPlanterm() {
		return planTerm;
	}

	public void setPlanterm(PlanTerm planterm) {
		this.planTerm = planterm;
	}

	public BigDecimal getTermChargeAmt() {
		return termChargeAmt;
	}

	public void setTermChargeAmt(BigDecimal termChargeAmt) {
		this.termChargeAmt = termChargeAmt;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSubscriptionStatus() {
		return subscriptionStatus;
	}

	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}

	public Date getSubscribedDate() {
		return subscribedDate;
	}

	public void setSubscribedDate(Date subscribedDate) {
		this.subscribedDate = subscribedDate;
	}

	public String getRenewalType() {
		return renewalType;
	}

	public void setRenewalType(String renewalType) {
		this.renewalType = renewalType;
	}

	public String getIsFree() {
		return isFree;
	}

	public void setIsFree(String isFree) {
		this.isFree = isFree;
	}

	public OrganizationSubscription getOrganizationSubscription() {
		return organizationSubscription;
	}

	public void setOrganizationSubscription(OrganizationSubscription organizationsubscription) {
		this.organizationSubscription = organizationsubscription;
	}

	public String getCurrentActiveSubscription() {
		return currentActiveSubscription;
	}

	public void setCurrentActiveSubscription(String currentActiveSubscription) {
		this.currentActiveSubscription = currentActiveSubscription;
	}

	public byte getActive() {
		return active;
	}

	public void setActive(byte active) {
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

}
