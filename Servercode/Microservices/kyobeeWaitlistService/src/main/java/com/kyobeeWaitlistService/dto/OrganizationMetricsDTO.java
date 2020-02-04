package com.kyobeeWaitlistService.dto;

public class OrganizationMetricsDTO {
	
	private Integer notifyUserCount;
	private Integer orgTotalWaitTime;
	private Integer orgGuestCount;
	private String guestToBeNotified;
	private Integer perPartyWaitTime;
	private Integer guestNotifiedWaitTime;
	private Integer guestMinRank;
	
	public Integer getNotifyUserCount() {
		return notifyUserCount;
	}
	public void setNotifyUserCount(Integer notifyUserCount) {
		this.notifyUserCount = notifyUserCount;
	}
	public Integer getOrgTotalWaitTime() {
		return orgTotalWaitTime;
	}
	public void setOrgTotalWaitTime(Integer orgTotalWaitTime) {
		this.orgTotalWaitTime = orgTotalWaitTime;
	}
	public Integer getOrgGuestCount() {
		return orgGuestCount;
	}
	public void setOrgGuestCount(Integer orgGuestCount) {
		this.orgGuestCount = orgGuestCount;
	}
	
	public String getGuestToBeNotified() {
		return guestToBeNotified;
	}
	public void setGuestToBeNotified(String guestToBeNotified) {
		this.guestToBeNotified = guestToBeNotified;
	}
	public Integer getPerPartyWaitTime() {
		return perPartyWaitTime;
	}
	public void setPerPartyWaitTime(Integer perPartyWaitTime) {
		this.perPartyWaitTime = perPartyWaitTime;
	}
	public Integer getGuestNotifiedWaitTime() {
		return guestNotifiedWaitTime;
	}
	public void setGuestNotifiedWaitTime(Integer guestNotifiedWaitTime) {
		this.guestNotifiedWaitTime = guestNotifiedWaitTime;
	}
	public Integer getGuestMinRank() {
		return guestMinRank;
	}
	public void setGuestMinRank(Integer guestMinRank) {
		this.guestMinRank = guestMinRank;
	}
	
}
