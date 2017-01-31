package com.kyobee.entity;

import java.io.Serializable;

public class GuestNotificationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String guestNoPrefix;
	private Long rank;
	private Long guestCount;
	private Long totalWaitTime;
	private String prefType;
	private String uuid;
	private String sms;
	private String email;
	private String notificationFlag;
	private Long guestNotifiedWaitTime;
	private String deviceType;
	private String deviceId;
	private Long orgId;
	private String smsSignature;
	private String smsRoute;
	private String clientBase;

	public String getSmsSignature() {
		return smsSignature;
	}
	public void setSmsSignature(String smsSignature) {
		this.smsSignature = smsSignature;
	}
	public String getSmsRoute() {
		return smsRoute;
	}
	public void setSmsRoute(String smsRoute) {
		this.smsRoute = smsRoute;
	}
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getGuestNoPrefix() {
		return guestNoPrefix;
	}
	public void setGuestNoPrefix(String guestNoPrefix) {
		this.guestNoPrefix = guestNoPrefix;
	}
	public Long getRank() {
		return rank;
	}
	public void setRank(Long rank) {
		this.rank = rank;
	}
	public Long getGuestCount() {
		return guestCount;
	}
	public void setGuestCount(Long guestCount) {
		this.guestCount = guestCount;
	}
	public Long getTotalWaitTime() {
		return totalWaitTime;
	}
	public void setTotalWaitTime(Long totalWaitTime) {
		this.totalWaitTime = totalWaitTime;
	}
	public String getPrefType() {
		return prefType;
	}
	public void setPrefType(String prefType) {
		this.prefType = prefType;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNotificationFlag() {
		return notificationFlag;
	}
	public void setNotificationFlag(String notificationFlag) {
		this.notificationFlag = notificationFlag;
	}
	public Long getGuestNotifiedWaitTime() {
		return guestNotifiedWaitTime;
	}
	public void setGuestNotifiedWaitTime(Long guestNotifiedWaitTime) {
		this.guestNotifiedWaitTime = guestNotifiedWaitTime;
	}
	public String getClientBase() {
		return clientBase;
	}
	public void setClientBase(String clientBase) {
		this.clientBase = clientBase;
	}
	
}
