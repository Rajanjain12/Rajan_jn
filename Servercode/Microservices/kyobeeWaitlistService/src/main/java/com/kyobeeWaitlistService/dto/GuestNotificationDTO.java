package com.kyobeeWaitlistService.dto;

public class GuestNotificationDTO {
	
	private Integer tempLevel;
	private String message;
	private String smsSignature;
	private String smsRoute;
	private String smsRouteNo;
	private Integer contactNo;
	private String prefType;
	private String deviceType;
	private String deviceId;
	
	public Integer getTempLevel() {
		return tempLevel;
	}
	public void setTempLevel(Integer tempLevel) {
		this.tempLevel = tempLevel;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
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
	public String getSmsRouteNo() {
		return smsRouteNo;
	}
	public void setSmsRouteNo(String smsRouteNo) {
		this.smsRouteNo = smsRouteNo;
	}
	public Integer getContactNo() {
		return contactNo;
	}
	public void setContactNo(Integer contactNo) {
		this.contactNo = contactNo;
	}
	public String getPrefType() {
		return prefType;
	}
	public void setPrefType(String prefType) {
		this.prefType = prefType;
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
	
}
