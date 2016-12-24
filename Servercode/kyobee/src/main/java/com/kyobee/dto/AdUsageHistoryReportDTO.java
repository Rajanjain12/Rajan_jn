package com.kyobee.dto;

import java.math.BigDecimal;

public class AdUsageHistoryReportDTO {

	private String activityDate;
	
	private String activityDesc;
	
	private Long adsCredit;
	
	private Long adsBalance;
	
	private BigDecimal amount;
	
	private String notes;

	public String getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(String activityDate) {
		this.activityDate = activityDate;
	}

	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}

	public Long getAdsCredit() {
		return adsCredit;
	}

	public void setAdsCredit(Long adsCredit) {
		this.adsCredit = adsCredit;
	}

	public Long getAdsBalance() {
		return adsBalance;
	}

	public void setAdsBalance(Long adsBalance) {
		this.adsBalance = adsBalance;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
