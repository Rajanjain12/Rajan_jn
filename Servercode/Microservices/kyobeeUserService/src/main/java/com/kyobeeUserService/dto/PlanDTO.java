package com.kyobeeUserService.dto;

public class PlanDTO {
	
	private Integer planID;
	private String planName;
	
	public Integer getPlanID() {
		return planID;
	}
	public void setPlanID(Integer planID) {
		this.planID = planID;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	
	public PlanDTO(Integer planID,String planName) {
		 this.planID=planID;
		 this.planName=planName;
	 }
}
