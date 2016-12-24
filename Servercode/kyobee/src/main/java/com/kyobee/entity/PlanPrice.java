package com.kyobee.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity(name="PlanPrice")
@Table(name="PLANPRICE")
@NamedQueries({
	@NamedQuery(name=PlanPrice.GET_PLAN_PRICE_FROM_PLAN,query="Select pp from PlanPrice pp left join fetch pp.unit unit where pp.plan.planId = ?1 "),
	@NamedQuery(name=PlanPrice.GET_PLAN_PRICE_ENTITY,query="Select pp from PlanPrice pp left join fetch pp.currency currency " +
			" left join fetch pp.unit unit where pp.planPriceId = ?1 "),
	@NamedQuery(name=PlanPrice.GET_FREE_PLAN_PRICE_ENTITY,query="Select pp from PlanPrice pp where pp.plan.planId=?1 ")
})
public class PlanPrice extends BaseEntity{
	
	public static final String GET_PLAN_PRICE_FROM_PLAN = "getPlanPriceFromPlan";
	
	public static final String GET_PLAN_PRICE_ENTITY = "getPlanPriceEntity";
	public static final String GET_FREE_PLAN_PRICE_ENTITY = "getFreePlanPriceEntity";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long planPriceId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PlanId")
	private Plan plan;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="UnitId")
	private Lookup unit;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="CurrencyId")
	private Lookup currency;
	
	@Column(name="AmountPerUnit")
	private BigDecimal amountPerUnit;
	
	@Column(name="NoOfAdsPerUnit")
	private Long noOfAdsPerUnit;
	
	@Column(name="CostPerAd")
	private BigDecimal costPerAd;

	public Long getPlanPriceId() {
		return planPriceId;
	}

	public void setPlanPriceId(Long planPriceId) {
		this.planPriceId = planPriceId;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Lookup getUnit() {
		return unit;
	}

	public void setUnit(Lookup unit) {
		this.unit = unit;
	}

	public Lookup getCurrency() {
		return currency;
	}

	public void setCurrency(Lookup currency) {
		this.currency = currency;
	}

	public BigDecimal getAmountPerUnit() {
		return amountPerUnit;
	}

	public void setAmountPerUnit(BigDecimal amountPerUnit) {
		this.amountPerUnit = amountPerUnit;
	}

	public Long getNoOfAdsPerUnit() {
		return noOfAdsPerUnit;
	}

	public void setNoOfAdsPerUnit(Long noOfAdsPerUnit) {
		this.noOfAdsPerUnit = noOfAdsPerUnit;
	}

	public BigDecimal getCostPerAd() {
		return costPerAd;
	}

	public void setCostPerAd(BigDecimal costPerAd) {
		this.costPerAd = costPerAd;
	}

	
	
	

}
