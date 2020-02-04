package com.kyobeeWaitlistService.dto;

import java.util.List;

public class GuestResponseDTO {
	
	private Integer totalRecords;
    private Integer pageNo;
    private List<GuestDTO> records;
	public Integer getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public List<GuestDTO> getRecords() {
		return records;
	}
	public void setRecords(List<GuestDTO> records) {
		this.records = records;
	}
    
    
}
