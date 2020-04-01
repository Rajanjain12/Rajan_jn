package com.kyobeeWaitlistService.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PLANTERM")
public class PlanTerm implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PlanTermID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer planTermID;

	@Column(name = "PlanTermName")
	private String planTermName;

	@Column(name = "SortOrder")
	private Integer sortOrder;

	@Column(name = "Active")
	private Byte active;

	@Temporal(TemporalType.DATE)
	@Column(name = "CreatedAt")
	private Date createdAt;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "ModifiedAt")
	private Date modifiedAt;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@OneToMany(mappedBy = "planterm")
	private List<OrganizationSubscriptionDetail> organizationSubscriptionDetails;

	// bi-directional many-to-one association to Planfeaturecharge
	@OneToMany(mappedBy = "planterm")
	private List<PlanFeatureCharge> planFeatureCharges;

	public Integer getPlanTermID() {
		return planTermID;
	}

	public void setPlanTermID(Integer planTermID) {
		this.planTermID = planTermID;
	}

	public String getPlanTermName() {
		return planTermName;
	}

	public void setPlanTermName(String planTermName) {
		this.planTermName = planTermName;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Byte getActive() {
		return active;
	}

	public void setActive(Byte active) {
		this.active = active;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public List<OrganizationSubscriptionDetail> getOrganizationSubscriptionDetails() {
		return organizationSubscriptionDetails;
	}

	public void setOrganizationSubscriptionDetails(
			List<OrganizationSubscriptionDetail> organizationSubscriptionDetails) {
		this.organizationSubscriptionDetails = organizationSubscriptionDetails;
	}

	public OrganizationSubscriptionDetail addOrganizationsubscriptiondetail(
			OrganizationSubscriptionDetail organizationsubscriptiondetail) {
		getOrganizationSubscriptionDetails().add(organizationsubscriptiondetail);
		organizationsubscriptiondetail.setPlanterm(this);

		return organizationsubscriptiondetail;
	}

	public OrganizationSubscriptionDetail removeOrganizationsubscriptiondetail(
			OrganizationSubscriptionDetail organizationsubscriptiondetail) {
		getOrganizationSubscriptionDetails().remove(organizationsubscriptiondetail);
		organizationsubscriptiondetail.setPlanterm(null);

		return organizationsubscriptiondetail;
	}

	public List<PlanFeatureCharge> getPlanFeatureCharges() {
		return planFeatureCharges;
	}

	public void setPlanFeatureCharges(List<PlanFeatureCharge> planFeatureCharges) {
		this.planFeatureCharges = planFeatureCharges;
	}

	public PlanFeatureCharge addPlanfeaturecharge(PlanFeatureCharge planfeaturecharge) {
		getPlanFeatureCharges().add(planfeaturecharge);
		planfeaturecharge.setPlanterm(this);

		return planfeaturecharge;
	}

	public PlanFeatureCharge removePlanfeaturecharge(PlanFeatureCharge planfeaturecharge) {
		getPlanFeatureCharges().remove(planfeaturecharge);
		planfeaturecharge.setPlanterm(null);

		return planfeaturecharge;
	}

}
