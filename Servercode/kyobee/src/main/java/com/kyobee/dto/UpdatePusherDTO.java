package com.kyobee.dto;

import com.kyobee.entity.Guest;

public class UpdatePusherDTO {

	private Integer nextToNotifyGuestId;
	private String OP;
	private Integer totalWaitTime;
	private Integer nowServingGuestId;
	private String from;
	private Guest updguest;
	private Long orgGuestCount;
	
	public Integer getNextToNotifyGuestId() {
		return nextToNotifyGuestId;
	}
	public void setNextToNotifyGuestId(Integer nextToNotifyGuestId) {
		this.nextToNotifyGuestId = nextToNotifyGuestId;
	}
	public String getOP() {
		return OP;
	}
	public void setOP(String oP) {
		OP = oP;
	}
	public Integer getTotalWaitTime() {
		return totalWaitTime;
	}
	public void setTotalWaitTime(Integer totalWaitTime) {
		this.totalWaitTime = totalWaitTime;
	}
	public Integer getNowServingGuestId() {
		return nowServingGuestId;
	}
	public void setNowServingGuestId(Integer nowServingGuestId) {
		this.nowServingGuestId = nowServingGuestId;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public Guest getUpdguest() {
		return updguest;
	}
	public void setUpdguest(Guest updguest) {
		this.updguest = updguest;
	}
	public Long getOrgGuestCount() {
		return orgGuestCount;
	}
	public void setOrgGuestCount(Long orgGuestCount) {
		this.orgGuestCount = orgGuestCount;
	}
	
	
}
