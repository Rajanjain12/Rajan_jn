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
@Table(name = "FEATURE")
public class Feature implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FeatureID")
	private Integer featureID;

	@Column(name = "FeatureName")
	private String featureName;

	@Column(name = "FeatureDescription")
	private String featureDescription;

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

	// bi-directional many-to-one association to OrganizationSubscriptionDetail
	@OneToMany(mappedBy = "feature")
	private List<OrganizationSubscriptionDetail> organizationSubscriptionDetails;

	// bi-directional many-to-one association to PlanFeatureCharge
	@OneToMany(mappedBy = "feature")
	private List<PlanFeatureCharge> planFeatureCharges;

	public Integer getFeatureID() {
		return featureID;
	}

	public void setFeatureID(Integer featureID) {
		this.featureID = featureID;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getFeatureDescription() {
		return featureDescription;
	}

	public void setFeatureDescription(String featureDescription) {
		this.featureDescription = featureDescription;
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

	public void setPlanFeatureCharges(List<PlanFeatureCharge> planFeatureCharges) {
		this.planFeatureCharges = planFeatureCharges;
	}

	public OrganizationSubscriptionDetail addOrganizationSubscriptionDetail(
			OrganizationSubscriptionDetail organizationsubscriptiondetail) {
		getOrganizationSubscriptionDetails().add(organizationsubscriptiondetail);
		organizationsubscriptiondetail.setFeature(this);

		return organizationsubscriptiondetail;
	}

	public OrganizationSubscriptionDetail removeOrganizationSubscriptionDetail(
			OrganizationSubscriptionDetail organizationsubscriptiondetail) {
		getOrganizationSubscriptionDetails().remove(organizationsubscriptiondetail);
		organizationsubscriptiondetail.setFeature(null);

		return organizationsubscriptiondetail;
	}

	public PlanFeatureCharge addPlanFeatureCharge(PlanFeatureCharge planfeaturecharge) {
		getPlanFeatureCharges().add(planfeaturecharge);
		planfeaturecharge.setFeature(this);

		return planfeaturecharge;
	}

	public PlanFeatureCharge removePlanFeatureCharge(PlanFeatureCharge planfeaturecharge) {
		getPlanFeatureCharges().remove(planfeaturecharge);
		planfeaturecharge.setFeature(null);

		return planfeaturecharge;
	}

}
