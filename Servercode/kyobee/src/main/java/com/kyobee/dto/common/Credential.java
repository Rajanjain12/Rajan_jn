package com.kyobee.dto.common;

import java.io.Serializable;
import java.util.List;

import com.kyobee.dto.AddressDTO;
import com.kyobee.dto.GuestPreferencesDTO;

public class Credential implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3073325363363636663L;

	//Aarshi
	private String userId;
	//Aarshi
	private String orgId;
	
	//Aarshi
	private AddressDTO addressDTO;
	
	

	public AddressDTO getAddressDTO() {
		return addressDTO;
	}

	public void setAddressDTO(AddressDTO addressDTO) {
		this.addressDTO = addressDTO;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}


	private String username;
	private String password;
	private String companyName;
	private String companyPrimaryPhone;
	private String companyEmail;
	private String promotionalCode;
	private String firstName;
	private String lastName;
	private String confirmPassword;
	private String clientBase;
	//Aarshi
	private String email;
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyPrimaryPhone() {
		return companyPrimaryPhone;
	}

	public void setCompanyPrimaryPhone(String companyPrimaryPhone) {
		this.companyPrimaryPhone = companyPrimaryPhone;
	}

	public String getPromotionalCode() {
		return promotionalCode;
	}

	public void setPromotionalCode(String promotionalCode) {
		this.promotionalCode = promotionalCode;
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getClientBase() {
		return clientBase;
	}

	public void setClientBase(String clientBase) {
		this.clientBase = clientBase;
	}
}
