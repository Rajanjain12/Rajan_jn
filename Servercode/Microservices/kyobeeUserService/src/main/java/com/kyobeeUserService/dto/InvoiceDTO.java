package com.kyobeeUserService.dto;

import java.util.List;

public class InvoiceDTO {
	List<Integer> featureChargeIds;
	Integer orgSubscriptionId;
	OrganizationDTO orgDTO;
	
	public List<Integer> getFeatureChargeIds() {
		return featureChargeIds;
	}
	public void setFeatureChargeIds(List<Integer> featureChargeIds) {
		this.featureChargeIds = featureChargeIds;
	}
	public Integer getOrgSubscriptionId() {
		return orgSubscriptionId;
	}
	public void setOrgSubscriptionId(Integer orgSubscriptionId) {
		this.orgSubscriptionId = orgSubscriptionId;
	}
	public OrganizationDTO getOrgDTO() {
		return orgDTO;
	}
	public void setOrgDTO(OrganizationDTO orgDTO) {
		this.orgDTO = orgDTO;
	}
	
}
