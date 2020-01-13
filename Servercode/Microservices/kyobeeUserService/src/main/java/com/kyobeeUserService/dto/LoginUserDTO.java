package com.kyobeeUserService.dto;

import java.util.List;


public class LoginUserDTO {
	
	    private Integer userID;
	    private String userName;
	    private String firstName;
	    private String lastName;
	
	    private String email;
	    private List<String> permissionList;
	    private Integer organizationID;
	    private String smsRoute;
	    private Integer maxParty;
	    private Integer defaultLangId;
	    private String clientBase;
	    private String companyEmail;
	    private String organizationName;
	    private List<SeatingMarketingPrefDTO> seatingpref;
	    private List<SeatingMarketingPrefDTO> marketingPref;
	    private List<SmsTemplateDTO> smsTemplate;
	    private List<LanguageKeyMappingDTO> languagePref;
	
		public List<LanguageKeyMappingDTO> getLanguagePref() {
			return languagePref;
		}
		public void setLanguagePref(List<LanguageKeyMappingDTO> languagePref) {
			this.languagePref = languagePref;
		}
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

		public Integer getOrganizationID() {
			return organizationID;
		}
		public void setOrganizationID(Integer organizationID) {
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


		public List<SeatingMarketingPrefDTO> getSeatingpref() {
			return seatingpref;
		}
		public void setSeatingpref(List<SeatingMarketingPrefDTO> seatingpref) {
			this.seatingpref = seatingpref;
		}
		public List<SeatingMarketingPrefDTO> getMarketingPref() {
			return marketingPref;
		}
		public void setMarketingPref(List<SeatingMarketingPrefDTO> marketingPref) {
			this.marketingPref = marketingPref;
		}
		public List<SmsTemplateDTO> getSmsTemplate() {
			return smsTemplate;
		}
		public void setSmsTemplate(List<SmsTemplateDTO> smsTemplate) {
			this.smsTemplate = smsTemplate;
		}

	    

}
