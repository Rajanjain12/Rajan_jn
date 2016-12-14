package com.rsnt.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.rsnt.common.entity.BaseEntity;

@Entity(name="OrganizationPlanSubscription")
@Table(name="ORGANIZATIONPLANSUBSCRIPTION")
@NamedQueries({
	@NamedQuery(name=OrganizationPlanSubscription.GET_ACTIVE_ORGANIZATION_PLAN_SUBSCRIPTION,
			query="Select Op from OrganizationPlanSubscription Op inner join fetch Op.plan plan where Op.organizationPlanId=?1 "),
	@NamedQuery(name=OrganizationPlanSubscription.GET_ORGANIZATION_PLAN_SUBSCRIPTION, query="SELECT Op from OrganizationPlanSubscription Op where Op.organizationPlanId=?1 ")		
	
})
public class OrganizationPlanSubscription extends BaseEntity {

	public static final String GET_ACTIVE_ORGANIZATION_PLAN_SUBSCRIPTION = "getActiveOrgPlanSubscription";
	
	public static final String GET_ORGANIZATION_PLAN_SUBSCRIPTION = "getOrgPlanSubscription";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="OrganizationPlanId")
	private Long organizationPlanId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="OrganizationId")
	private Organization organization;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PlanId")
	private Plan plan;
	
	@Column(name="UnitId")
	private Long unitId;
	
	@Column(name="AmountPerUnit")
	private BigDecimal amountPerUnit;
	
	@Column(name="CurrencyId")
	private Long currencyId;
	
	@Column(name="NoOfUnit")
	private Long numberOfUnits;
	
	@Column(name="TotalAmount")
	private BigDecimal totalAmount;
	
	@Column(name="StartDate")
	private Date startDate;
	
	@Column(name="EndDate")
	private Date endDate;
	
	@Column(name="TerminateDate")
	private Date terminateDate;
	
	@Column(name="CostPerAd")
	private BigDecimal costPerAd;
	
	@Column(name="NoOfAdsPerUnit")
	private Long noOfAdsPerUnit;
	
	@Column(name="ChargeId")
	private String chargeId;
	

	public Long getOrganizationPlanId() {
		return organizationPlanId;
	}

	public void setOrganizationPlanId(Long organizationPlanId) {
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

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public BigDecimal getAmountPerUnit() {
		return amountPerUnit;
	}

	public void setAmountPerUnit(BigDecimal amountPerUnit) {
		this.amountPerUnit = amountPerUnit;
	}

	public Long getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	public Long getNumberOfUnits() {
		return numberOfUnits;
	}

	public void setNumberOfUnits(Long numberOfUnits) {
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

	public Long getNoOfAdsPerUnit() {
		return noOfAdsPerUnit;
	}

	public void setNoOfAdsPerUnit(Long noOfAdsPerUnit) {
		this.noOfAdsPerUnit = noOfAdsPerUnit;
	}


	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}



	
	
	
}
