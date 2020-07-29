package com.kyobeeAccountService.dto;

public class CredentialsDTO {

       String userName;
       String password;
       String deviceType;
       String deviceToken;
       String clientBase;
       
       
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public String getClientBase() {
		return clientBase;
	}
	public void setClientBase(String clientBase) {
		this.clientBase = clientBase;
	}
       
       
}
