package com.kyobeeUserService.dto;

import java.util.List;

import com.kyobeeUserService.entity.Address;

public class LoginUserDTO {
	
	    private Integer userID;
	    private String userName;
	    private String firstName;
	    private String lastName;
	/*
	 * private Address address; private String primaryContactNo; private String
	 * alternateContactNo;
	 */
	    private String email;
	    private List<String> permissionList;
	    private Long organizationID;
	    private String smsRoute;
	    private Integer maxParty;
	    private Integer defaultLangId;
	    private String clientBase;
	    private String companyEmail;
	    private String organizationName;
	    private String seatingpref;
	    private MarketingPreferenceDTO marketingPref;
	    private String smsTemplate;
	
		public Integer getUserID() {
			return userID;
		}
		public void setUserID(Integer userID) {
			this.userID = userID;
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

	/*
	 * public Address getAddress() { return address; } public void
	 * setAddress(Address address) { this.address = address; } public String
	 * getPrimaryContactNo() { return primaryContactNo; } public void
	 * setPrimaryContactNo(String primaryContactNo) { this.primaryContactNo =
	 * primaryContactNo; } public String getAlternateContactNo() { return
	 * alternateContactNo; } public void setAlternateContactNo(String
	 * alternateContactNo) { this.alternateContactNo = alternateContactNo; }
	 */
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public List<String> getPermissionList() {
			return permissionList;
		}
		public void setPermissionList(List<String> permissionList) {
			this.permissionList = permissionList;
		}

		public Long getOrganizationID() {
			return organizationID;
		}
		public void setOrganizationID(Long organizationID) {
			this.organizationID = organizationID;
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
		public String getCompanyEmail() {
			return companyEmail;
		}
		public void setCompanyEmail(String companyEmail) {
			this.companyEmail = companyEmail;
		}

		public String getOrganizationName() {
			return organizationName;
		}
		public void setOrganizationName(String organizationName) {
			this.organizationName = organizationName;
		}
		public String getSeatingpref() {
			return seatingpref;
		}
		public void setSeatingpref(String seatingpref) {
			this.seatingpref = seatingpref;
		}
		public MarketingPreferenceDTO getMarketingPref() {
			return marketingPref;
		}
		public void setMarketingPref(MarketingPreferenceDTO marketingPref) {
			this.marketingPref = marketingPref;
		}
		public String getSmsTemplate() {
			return smsTemplate;
		}
		public void setSmsTemplate(String smsTemplate) {
			this.smsTemplate = smsTemplate;
		}
	    
	    

}
