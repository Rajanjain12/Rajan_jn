package com.kyobeeWaitlistService.dto;

public class OrgSettingPusherDTO {
	
	private String  op;
	private OrgSettingDTO orgSettingDTO;
	
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public OrgSettingDTO getOrgSettingDTO() {
		return orgSettingDTO;
	}
	public void setOrgSettingDTO(OrgSettingDTO orgSettingDTO) {
		this.orgSettingDTO = orgSettingDTO;
	}

}
