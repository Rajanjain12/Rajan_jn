package com.kyobeeAccountService.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.OneToMany;

@Entity
@Table(name = "PLANFEATURECHARGE")
public class PlanFeatureCharge implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PlanFeatureChargeID")
	private int planFeatureChargeID;

	// bi-directional many-to-one association to Feature
	@ManyToOne
	@JoinColumn(name = "FeatureID")
	private Feature feature;

	// bi-directional many-to-one association to Plan
	@ManyToOne
	@JoinColumn(name = "PlanID")
	private Plan plan;

	@ManyToOne
	@JoinColumn(name = "CurrencyID")
	private Currency currency;

	// bi-directional many-to-one association to Planterm
	@ManyToOne
	@JoinColumn(name = "PlanTermID")
	private PlanTerm planTerm;

	@Column(name = "TermChargeAmt")
	private BigDecimal termChargeAmt;

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

	// bi-directional many-to-one association to Organizationsubscriptiondetail
	@OneToMany(mappedBy = "planFeatureCharge")
	private List<OrganizationSubscriptionDetail> organizationSubscriptionDetails;

	public int getPlanFeatureChargeID() {
		return planFeatureChargeID;
	}

	public void setPlanFeatureChargeID(int planFeatureChargeID) {
		this.planFeatureChargeID = planFeatureChargeID;
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

	public List<OrganizationSubscriptionDetail> getOrganizationsubscriptiondetails() {
		return this.organizationSubscriptionDetails;
	}

	public void setOrganizationSubscriptionDetails(
			List<OrganizationSubscriptionDetail> organizationSubscriptionDetails) {
		this.organizationSubscriptionDetails = organizationSubscriptionDetails;
	}

	public OrganizationSubscriptionDetail addOrganizationSubscriptionDetail(
			OrganizationSubscriptionDetail organizationSubscriptionDetail) {
		getOrganizationsubscriptiondetails().add(organizationSubscriptionDetail);
		organizationSubscriptionDetail.setPlanFeatureCharge(this);

		return organizationSubscriptionDetail;
	}

	public OrganizationSubscriptionDetail removeOrganizationSubscriptionDsssetail(
			OrganizationSubscriptionDetail organizationSubscriptionDetail) {
		getOrganizationsubscriptiondetails().remove(organizationSubscriptionDetail);
		organizationSubscriptionDetail.setPlanFeatureCharge(null);

		return organizationSubscriptionDetail;
	}

}
