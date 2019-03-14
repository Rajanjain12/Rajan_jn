package com.kyobee.dto;

/**
 * Dummy class just to create the structure. 
 * We can remove it once we have actual DTO's in place
 * @author rohit
 *
 */
import java.util.List;

public class UserDTO {

	private Long userId;

	private String userName;

	private String firstName;

	private String lastName;

	private String address;

	private String primaryContactNo;

	private String alternateContactNo;

	private String email;

	private boolean active;

	private List<String> permissionList;

	private String activationId;

	private Long organizationId;
	//Added Later
	private String smsRoute;
	
	private Integer maxParty;
	
	private Integer defaultLangId;
	
	private String clientBase;

	
	//Aarshi
	private String authCode;
	
	//change password by Aarshi(13/03/2019)
	private String password;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPrimaryContactNo() {
		return primaryContactNo;
	}

	public void setPrimaryContactNo(String primaryContactNo) {
		this.primaryContactNo = primaryContactNo;
	}

	public String getAlternateContactNo() {
		return alternateContactNo;
	}

	public void setAlternateContactNo(String alternateContactNo) {
		this.alternateContactNo = alternateContactNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<String> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
	}

	public String getActivationId() {
		return activationId;
	}

	public void setActivationId(String activationId) {
		this.activationId = activationId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getSmsRoute() {
		return smsRoute;
	}

	public void setSmsRoute(String smsRoute) {
		this.smsRoute = smsRoute;
	}

	public Integer getMaxParty() {
		return maxParty;
	}

	public void setMaxParty(Integer maxParty) {
		this.maxParty = maxParty;
	}
	
	public Integer getDefaultLangId() {
		return defaultLangId;
	}

	public void setDefaultLangId(Integer defaultLangId) {
		this.defaultLangId = defaultLangId;
	}

	public String getClientBase() {
		return clientBase;
	}

	public void setClientBase(String clientBase) {
		this.clientBase = clientBase;
	}

}
