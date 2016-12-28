package com.kyobee.dto;

public class WaitlistMetrics {
	private int guestId;
	private int nowServingParty;
	private int totalWaitTime;
	private int guestToBeNotified;
	private int totalWaitingGuest;
	private int noOfPartiesAhead;
	private int guestRank;
	private int guestNotifiedWaitTime;
	
	public int getGuestId() {
		return guestId;
	}
	public void setGuestId(int guestId) {
		this.guestId = guestId;
	}
	public int getNowServingParty() {
		return nowServingParty;
	}
	public void setNowServingParty(int nowServingParty) {
		this.nowServingParty = nowServingParty;
	}
	public int getTotalWaitTime() {
		return totalWaitTime;
	}
	public void setTotalWaitTime(int totalWaitTime) {
		this.totalWaitTime = totalWaitTime;
	}
	public int getGuestToBeNotified() {
		return guestToBeNotified;
	}
	public void setGuestToBeNotified(int guestToBeNotified) {
		this.guestToBeNotified = guestToBeNotified;
	}
	public int getTotalWaitingGuest() {
		return totalWaitingGuest;
	}
	public void setTotalWaitingGuest(int totalWaitingGuest) {
		this.totalWaitingGuest = totalWaitingGuest;
	}
	public int getNoOfPartiesAhead() {
		return noOfPartiesAhead;
	}
	public void setNoOfPartiesAhead(int noOfPartiesAhead) {
		this.noOfPartiesAhead = noOfPartiesAhead;
	}
	public int getGuestRank() {
		return guestRank;
	}
	public void setGuestRank(int guestRank) {
		this.guestRank = guestRank;
	}
	public int getGuestNotifiedWaitTime() {
		return guestNotifiedWaitTime;
	}
	public void setGuestNotifiedWaitTime(int guestNotifiedWaitTime) {
		this.guestNotifiedWaitTime = guestNotifiedWaitTime;
	}
	
}
