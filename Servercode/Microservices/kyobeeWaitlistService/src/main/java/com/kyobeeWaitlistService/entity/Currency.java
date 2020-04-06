package com.kyobeeWaitlistService.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CURRENCY")
public class Currency implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CurrencyID")
	private Integer currencyID;

	@Column(name = "CurrencyName")
	private String currencyName;

	@Column(name = "Code")
	private String code;

	@Column(name = "Icon")
	private String icon;

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

	// bi-directional many-to-one association to Country
	@OneToMany(mappedBy = "currency")
	private List<Country> countries;

	// bi-directional many-to-one association to OrganizationSubscriptionDetail
	@OneToMany(mappedBy = "currency")
	private List<OrganizationSubscriptionDetail> organizationSubscriptionDetails;

	// bi-directional many-to-one association to PlanFeatureCharge
	@OneToMany(mappedBy = "currency")
	private List<PlanFeatureCharge> planFeatureCharges;

	// bi-directional many-to-one association to Promotionalcode
	@OneToMany(mappedBy = "currency")
	private List<PromotionalCode> promotionalCodes;

	public Integer getCurrencyID() {
		return currencyID;
	}

	public void setCurrencyID(Integer currencyID) {
		this.currencyID = currencyID;
	}
   
	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public OrganizationSubscriptionDetail addOrganizationSubscriptionDetail(
			OrganizationSubscriptionDetail organizationsubscriptiondetail) {
		getOrganizationSubscriptionDetails().add(organizationsubscriptiondetail);
		organizationsubscriptiondetail.setCurrency(this);

		return organizationsubscriptiondetail;
	}

	public OrganizationSubscriptionDetail removeOrganizationSubscriptionDetail(
			OrganizationSubscriptionDetail organizationsubscriptiondetail) {
		getOrganizationSubscriptionDetails().remove(organizationsubscriptiondetail);
		organizationsubscriptiondetail.setCurrency(null);

		return organizationsubscriptiondetail;
	}

	public List<OrganizationSubscriptionDetail> getOrganizationSubscriptionDetails() {
		return organizationSubscriptionDetails;
	}

	public void setOrganizationSubscriptionDetails(
			List<OrganizationSubscriptionDetail> organizationSubscriptionDetails) {
		this.organizationSubscriptionDetails = organizationSubscriptionDetails;
	}

	public List<PlanFeatureCharge> getPlanFeatureCharges() {
		return planFeatureCharges;
	}

	public PlanFeatureCharge addPlanFeatureCharge(PlanFeatureCharge planfeaturecharge) {
		getPlanFeatureCharges().add(planfeaturecharge);
		planfeaturecharge.setCurrency(this);

		return planfeaturecharge;
	}

	public PlanFeatureCharge removePlanFeatureCharge(PlanFeatureCharge planfeaturecharge) {
		getPlanFeatureCharges().remove(planfeaturecharge);
		planfeaturecharge.setCurrency(null);

		return planfeaturecharge;
	}

	public void setPlanFeatureCharges(List<PlanFeatureCharge> planFeatureCharges) {
		this.planFeatureCharges = planFeatureCharges;
	}

	public List<PromotionalCode> getPromotionalCodes() {
		return promotionalCodes;
	}

	public void setPromotionalCodes(List<PromotionalCode> promotionalCodes) {
		this.promotionalCodes = promotionalCodes;
	}

	public PromotionalCode addPromotionalcode(PromotionalCode promotionalcode) {
		getPromotionalCodes().add(promotionalcode);
		promotionalcode.setCurrency(this);

		return promotionalcode;
	}

	public PromotionalCode removePromotionalcode(PromotionalCode promotionalcode) {
		getPromotionalCodes().remove(promotionalcode);
		promotionalcode.setCurrency(null);

		return promotionalcode;
	}

}
