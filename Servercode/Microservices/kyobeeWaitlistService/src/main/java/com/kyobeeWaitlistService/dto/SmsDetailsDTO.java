package com.kyobeeWaitlistService.dto;

public class SmsDetailsDTO {

	private String smsSignature;
	private String smsRoute;
	private String smsRouteNo;
	public SmsDetailsDTO()
	{
		
	}
	
	public SmsDetailsDTO(String smsSignature, String smsRoute, String smsRouteNo) {
		this.smsSignature = smsSignature;
		this.smsRoute = smsRoute;
		this.smsRouteNo = smsRouteNo;
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
	
}
