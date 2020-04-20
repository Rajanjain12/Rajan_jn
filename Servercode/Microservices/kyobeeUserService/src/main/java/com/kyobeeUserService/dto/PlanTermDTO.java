package com.kyobeeUserService.dto;

import java.util.List;

public class PlanTermDTO {

	private Integer termID;
	private String termName;
	List<PlanFeatureDTO> featureList;
	public Integer getTermID() {
		return termID;
	}
	public void setTermID(Integer termID) {
		this.termID = termID;
	}
	public String getTermName() {
		return termName;
	}
	public void setTermName(String termName) {
		this.termName = termName;
	}
	public List<PlanFeatureDTO> getFeatureList() {
		return featureList;
	}
	public void setFeatureList(List<PlanFeatureDTO> featureList) {
		this.featureList = featureList;
	}
	
	
}
