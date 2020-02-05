package com.kyobeeWaitlistService.dto;

public class WaitlistMetrics {

	private Integer guestId;
	private Integer nowServingParty;
	private Integer totalWaitTime;
	private Integer guestToBeNotified;
	private Integer totalWaitingGuest;
	private Integer noOfPartiesAhead;
	private Integer guestRank;
	private Integer guestNotifiedWaitTime;
	private String clientBase;
	public Integer getGuestId() {
		return guestId;
	}
	public void setGuestId(Integer guestId) {
		this.guestId = guestId;
	}
	public Integer getNowServingParty() {
		return nowServingParty;
	}
	public void setNowServingParty(Integer nowServingParty) {
		this.nowServingParty = nowServingParty;
	}
	public Integer getTotalWaitTime() {
		return totalWaitTime;
	}
	public void setTotalWaitTime(Integer totalWaitTime) {
		this.totalWaitTime = totalWaitTime;
	}
	public Integer getGuestToBeNotified() {
		return guestToBeNotified;
	}
	public void setGuestToBeNotified(Integer guestToBeNotified) {
		this.guestToBeNotified = guestToBeNotified;
	}
	public Integer getTotalWaitingGuest() {
		return totalWaitingGuest;
	}
	public void setTotalWaitingGuest(Integer totalWaitingGuest) {
		this.totalWaitingGuest = totalWaitingGuest;
	}
	public Integer getNoOfPartiesAhead() {
		return noOfPartiesAhead;
	}
	public void setNoOfPartiesAhead(Integer noOfPartiesAhead) {
		this.noOfPartiesAhead = noOfPartiesAhead;
	}
	public Integer getGuestRank() {
		return guestRank;
	}
	public void setGuestRank(Integer guestRank) {
		this.guestRank = guestRank;
	}
	public Integer getGuestNotifiedWaitTime() {
		return guestNotifiedWaitTime;
	}
	public void setGuestNotifiedWaitTime(Integer guestNotifiedWaitTime) {
		this.guestNotifiedWaitTime = guestNotifiedWaitTime;
	}
	public String getClientBase() {
		return clientBase;
	}
	public void setClientBase(String clientBase) {
		this.clientBase = clientBase;
	}
	
	
}
