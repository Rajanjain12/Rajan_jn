package com.kyobeeWaitlistService.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ORGANIZATIONPLANSUBSCRIPTION")
public class OrganizationPlanSubscription implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "OrganizationPlanId")
	private Integer organizationPlanId;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "OrganizationId")
	private Organization organization;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "PlanID")
	private Plan plan;

	@Column(name = "UnitId")
	private Integer unitId;

	@Column(name = "AmountPerUnit")
	private BigDecimal amountPerUnit;

	@Column(name = "CurrencyId")
	private Integer currencyId;

	@Column(name = "ChargeID")
	private String chargeId;

	@Column(name = "NoOfUnit")
	private Integer numberOfUnits;

	@Column(name = "TotalAmount")
	private BigDecimal totalAmount;

	@Temporal(TemporalType.DATE)
	@Column(name = "StartDate")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "EndDate")
	private Date endDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "TerminateDate")
	private Date terminateDate;

	@Column(name = "CostPerAd")
	private BigDecimal costPerAd;

	@Column(name = "NoOfAdsPerUnit")
	private Integer noOfAdsPerUnit;

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

	public Integer getOrganizationPlanId() {
		return organizationPlanId;
	}

	public void setOrganizationPlanId(Integer organizationPlanId) {
		this.organizationPlanId = organizationPlanId;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public BigDecimal getAmountPerUnit() {
		return amountPerUnit;
	}

	public void setAmountPerUnit(BigDecimal amountPerUnit) {
		this.amountPerUnit = amountPerUnit;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getNumberOfUnits() {
		return numberOfUnits;
	}

	public void setNumberOfUnits(Integer numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
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

	public Date getTerminateDate() {
		return terminateDate;
	}

	public void setTerminateDate(Date terminateDate) {
		this.terminateDate = terminateDate;
	}

	public BigDecimal getCostPerAd() {
		return costPerAd;
	}

	public void setCostPerAd(BigDecimal costPerAd) {
		this.costPerAd = costPerAd;
	}

	public Integer getNoOfAdsPerUnit() {
		return noOfAdsPerUnit;
	}

	public void setNoOfAdsPerUnit(Integer noOfAdsPerUnit) {
		this.noOfAdsPerUnit = noOfAdsPerUnit;
	}

	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
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

}
