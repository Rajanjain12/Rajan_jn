package com.kyobeeUserService.dto;

import java.math.BigDecimal;

public class PlanFeatureChargeDTO {
	private Integer planId;
	private String planName;
	private BigDecimal featureCharge;
	private Integer currencyId;
	private String currencyIcon;
	private Integer planFeatureChargeId;
	
	public Integer getPlanId() {
		return planId;
	}
	public void setPlanId(Integer planId) {
		this.planId = planId;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public BigDecimal getFeatureCharge() {
		return featureCharge;
	}
	public void setFeatureCharge(BigDecimal featureCharge) {
		this.featureCharge = featureCharge;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public String getCurrencyIcon() {
		return currencyIcon;
	}
	public void setCurrencyIcon(String currencyIcon) {
		this.currencyIcon = currencyIcon;
	}
	public Integer getPlanFeatureChargeId() {
		return planFeatureChargeId;
	}
	public void setPlanFeatureChargeId(Integer planFeatureChargeId) {
		this.planFeatureChargeId = planFeatureChargeId;
	}
	

}
