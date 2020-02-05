package com.kyobeeWaitlistService.dto;

public class GuestHistoryRequestDTO {
	private Integer orgId;
	private Integer pageSize;
	private Integer pageNo;
	private String clientTimezone;
	private Integer sliderMaxTime;
	private Integer sliderMinTime;
	private String statusOption;
	private String searchText;
	
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public String getClientTimezone() {
		return clientTimezone;
	}
	public void setClientTimezone(String clientTimezone) {
		this.clientTimezone = clientTimezone;
	}
	public Integer getSliderMaxTime() {
		return sliderMaxTime;
	}
	public void setSliderMaxTime(Integer sliderMaxTime) {
		this.sliderMaxTime = sliderMaxTime;
	}
	public Integer getSliderMinTime() {
		return sliderMinTime;
	}
	public void setSliderMinTime(Integer sliderMinTime) {
		this.sliderMinTime = sliderMinTime;
	}
	public String getStatusOption() {
		return statusOption;
	}
	public void setStatusOption(String statusOption) {
		this.statusOption = statusOption;
	}
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

}
