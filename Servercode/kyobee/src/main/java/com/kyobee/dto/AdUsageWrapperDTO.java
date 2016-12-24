package com.kyobee.dto;

import java.util.List;

public class AdUsageWrapperDTO {

	private List<AdUsageHistoryReportDTO> adUsageHistoryReportTOList;
	
	private String address;

	private String planName;

	private String dateSpan;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<AdUsageHistoryReportDTO> getAdUsageHistoryReportTOList() {
		return adUsageHistoryReportTOList;
	}

	public void setAdUsageHistoryReportTOList(
			List<AdUsageHistoryReportDTO> adUsageHistoryReportTOList) {
		this.adUsageHistoryReportTOList = adUsageHistoryReportTOList;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getDateSpan() {
		return dateSpan;
	}

	public void setDateSpan(String dateSpan) {
		this.dateSpan = dateSpan;
	}
}
