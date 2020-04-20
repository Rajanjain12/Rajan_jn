package com.kyobeeUserService.dto;

import java.util.List;

public class PlanDetailsDTO {
	
	private List<PlanDTO> planList;
	private List<PlanTermDTO> termList;
	
	public List<PlanDTO> getPlanList() {
		return planList;
	}
	public void setPlanList(List<PlanDTO> planList) {
		this.planList = planList;
	}
	public List<PlanTermDTO> getTermList() {
		return termList;
	}
	public void setTermList(List<PlanTermDTO> termList) {
		this.termList = termList;
	}
	
}
