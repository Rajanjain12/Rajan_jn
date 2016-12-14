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

import com.rsnt.common.entity.BaseEntity;

@Entity(name="OrganizationAdCredit")
@Table(name="ORGANIZATIONADCREDIT")
public class OrganizationAdCredit extends BaseEntity  {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OrganizationAdCreditID")
	private Long organizationAdCreditId;
	
	@JoinColumn(name="OrganizationId")
	@ManyToOne(fetch = FetchType.LAZY,cascade={})
	private Organization organization;
	
	@Column(name="CostPerAd")
	private BigDecimal costPerAd;
	
	@Column(name="CurrencyID")
	private Long currencyId;
	
	@Column(name="NoOfAds")
	private Long noOfAds;
	
	@Column(name="transactionDate")
	private Date transactionDate;
	
	@Column(name="TotalAmount")
	private BigDecimal totalAmount;
	
	@Column(name="OrganizationPlanId")
	private Long organizationPlanId;
	
	@Column(name="CreditType")
	private Long creditType;
	
	@Column(name="ChargeID")
	private String chargeId;

	public Long getOrganizationAdCreditId() {
		return organizationAdCreditId;
	}

	public void setOrganizationAdCreditId(Long organizationAdCreditId) {
		this.organizationAdCreditId = organizationAdCreditId;
	}


	public BigDecimal getCostPerAd() {
		return costPerAd;
	}

	public void setCostPerAd(BigDecimal costPerAd) {
		this.costPerAd = costPerAd;
	}

	public Long getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	public Long getNoOfAds() {
		return noOfAds;
	}

	public void setNoOfAds(Long noOfAds) {
		this.noOfAds = noOfAds;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Long getCreditType() {
		return creditType;
	}

	public void setCreditType(Long creditType) {
		this.creditType = creditType;
	}

	public Long getOrganizationPlanId() {
		return organizationPlanId;
	}

	public void setOrganizationPlanId(Long organizationPlanId) {
		this.organizationPlanId = organizationPlanId;
	}

	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}
	
	
}
