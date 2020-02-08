package com.kyobeeWaitlistService.dto;

public class WaitListMetricsDTO {

	private Integer nowServingParty;
	private Integer totalWaitTime;
	private Integer guestToBeNotified;
	private Integer totalWaitingGuest;
	private Integer guestNotifiedWaitTime;
	private String clientBase;
	private Integer perPartyWaitTime;
	private Integer orgId;
	private String op;
	private String from;

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

	public Integer getPerPartyWaitTime() {
		return perPartyWaitTime;
	}

	public void setPerPartyWaitTime(Integer perPartyWaitTime) {
		this.perPartyWaitTime = perPartyWaitTime;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
	
}
