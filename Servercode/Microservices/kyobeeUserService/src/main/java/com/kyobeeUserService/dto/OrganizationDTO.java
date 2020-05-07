package com.kyobeeUserService.dto;

import java.math.BigInteger;

public class OrganizationDTO {

	private AddressDTO addressDTO;
	private String email;
	private String organizationName;
	private BigInteger primaryPhone;
	private BigInteger secondaryPhone;
	private Integer orgId;
	private Integer customerId;
	private Integer orgTypeId;

	public AddressDTO getAddressDTO() {
		return addressDTO;
	}

	public void setAddressDTO(AddressDTO addressDTO) {
		this.addressDTO = addressDTO;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public BigInteger getPrimaryPhone() {
		return primaryPhone;
	}

	public void setPrimaryPhone(BigInteger primaryPhone) {
		this.primaryPhone = primaryPhone;
	}

	public BigInteger getSecondaryPhone() {
		return secondaryPhone;
	}

	public void setSecondaryPhone(BigInteger secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getOrgTypeId() {
		return orgTypeId;
	}

	public void setOrgTypeId(Integer orgTypeId) {
		this.orgTypeId = orgTypeId;
	}
	

}
