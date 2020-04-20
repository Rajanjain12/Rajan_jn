package com.kyobeeUserService.dto;

import java.util.List;

public class PlanFeatureDTO {
	
	private Integer featureId;
	private String featureName;
	private String featureDesc;
	List<PlanFeatureChargeDTO> featureChargeDetails;
	
	public Integer getFeatureId() {
		return featureId;
	}
	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	public String getFeatureDesc() {
		return featureDesc;
	}
	public void setFeatureDesc(String featureDesc) {
		this.featureDesc = featureDesc;
	}
	public List<PlanFeatureChargeDTO> getFeatureChargeDetails() {
		return featureChargeDetails;
	}
	public void setFeatureChargeDetails(List<PlanFeatureChargeDTO> featureChargeDetails) {
		this.featureChargeDetails = featureChargeDetails;
	}
	
}
