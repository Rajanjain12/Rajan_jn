package com.rsnt.entity;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;

import com.rsnt.common.entity.BaseEntity;

@Entity
@Table(name="ORGANIZATIONPLANSUBSCRIPTIONINVOICE")
@Name("OrganizationPlanSubscriptionInvoice")
public class OrganizationPlanSubscriptionInvoice extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OrganizationPlanSubscriptionInvoiceID")
	private Long OrganizationPlanSubscriptionInvoiceId;
	
	@JoinColumn(name="OrganizationID")
	@ManyToOne(fetch = FetchType.LAZY,cascade={})
	private Organization organization;
	
	@Column(name="PreviousPlanName")
	private String previousPlanName;
	
	@Column(name="CurrentPlanName")
	private String currentPlanName;
	
	@Column(name="PreviousPlanID")
	private Long previousPlanId;
	
	@Column(name="CurrentPlanID")
	private Long currentPlanId;
	
	@Column(name="InvoiceDate")
	private Date invoiceDate;
	
	@Column(name="Action")
	private String action;
	
	@Column(name="NoOfPlanDaysAdjustment")
	private Long noOfPlanDaysAdjustment;
	
	@Column(name="costPerDayAdjustment")
	private BigDecimal costPerDayAdjustment;
	
	@Column(name="PlanAmountAdjustment")
	private BigDecimal  planAmountAdjustment;
	
	@Column(name="NoOfAdsAdjustment")
	private Long  noOfAdsAdjustment;
	
	@Column(name="CostPerAdAdjustment")
	private BigDecimal costPerAdAdjustment;
	
	@Column(name="AdsAmountAdjustment")
	private BigDecimal adsAmountAdjustment;
	
	@Column(name="TotalAdjustment")
	private BigDecimal totalAdjustment;
	
	@Column(name="TotalActivePlanAmount")
	private BigDecimal totalActivePlanAmount;
	
	@Column(name="TotalAmount")
	private BigDecimal totalAmount;

	public Long getOrganizationPlanSubscriptionInvoiceId() {
		return OrganizationPlanSubscriptionInvoiceId;
	}

	public void setOrganizationPlanSubscriptionInvoiceId(
			Long organizationPlanSubscriptionInvoiceId) {
		OrganizationPlanSubscriptionInvoiceId = organizationPlanSubscriptionInvoiceId;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getPreviousPlanName() {
		return previousPlanName;
	}

	public void setPreviousPlanName(String previousPlanName) {
		this.previousPlanName = previousPlanName;
	}

	public String getCurrentPlanName() {
		return currentPlanName;
	}

	public void setCurrentPlanName(String currentPlanName) {
		this.currentPlanName = currentPlanName;
	}

	public Long getPreviousPlanId() {
		return previousPlanId;
	}

	public void setPreviousPlanId(Long previousPlanId) {
		this.previousPlanId = previousPlanId;
	}

	public Long getCurrentPlanId() {
		return currentPlanId;
	}

	public void setCurrentPlanId(Long currentPlanId) {
		this.currentPlanId = currentPlanId;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getNoOfPlanDaysAdjustment() {
		return noOfPlanDaysAdjustment;
	}

	public void setNoOfPlanDaysAdjustment(Long noOfPlanDaysAdjustment) {
		this.noOfPlanDaysAdjustment = noOfPlanDaysAdjustment;
	}

	public BigDecimal getCostPerDayAdjustment() {
		return costPerDayAdjustment;
	}

	public void setCostPerDayAdjustment(BigDecimal costPerDayAdjustment) {
		this.costPerDayAdjustment = costPerDayAdjustment;
	}

	public Long getNoOfAdsAdjustment() {
		return noOfAdsAdjustment;
	}

	public void setNoOfAdsAdjustment(Long noOfAdsAdjustment) {
		this.noOfAdsAdjustment = noOfAdsAdjustment;
	}

	public BigDecimal getCostPerAdAdjustment() {
		return costPerAdAdjustment;
	}

	public void setCostPerAdAdjustment(BigDecimal costPerAdAdjustment) {
		this.costPerAdAdjustment = costPerAdAdjustment;
	}

	public BigDecimal getAdsAmountAdjustment() {
		return adsAmountAdjustment;
	}

	public void setAdsAmountAdjustment(BigDecimal adsAmountAdjustment) {
		this.adsAmountAdjustment = adsAmountAdjustment;
	}

	public BigDecimal getTotalAdjustment() {
		return totalAdjustment;
	}

	public void setTotalAdjustment(BigDecimal totalAdjustment) {
		this.totalAdjustment = totalAdjustment;
	}

	public BigDecimal getTotalActivePlanAmount() {
		return totalActivePlanAmount;
	}

	public void setTotalActivePlanAmount(BigDecimal totalActivePlanAmount) {
		this.totalActivePlanAmount = totalActivePlanAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setPlanAmountAdjustment(BigDecimal planAmountAdjustment) {
		this.planAmountAdjustment = planAmountAdjustment;
	}
	
}

