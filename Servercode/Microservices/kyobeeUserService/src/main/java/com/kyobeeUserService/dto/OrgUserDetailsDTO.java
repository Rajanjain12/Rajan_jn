package com.kyobeeUserService.dto;

public class OrgUserDetailsDTO {
	private OrganizationDTO orgDTO;
	private CredentialsDTO credDTO;
	public OrganizationDTO getOrgDTO() {
		return orgDTO;
	}
	public void setOrgDTO(OrganizationDTO orgDTO) {
		this.orgDTO = orgDTO;
	}
	public CredentialsDTO getCredDTO() {
		return credDTO;
	}
	public void setCredDTO(CredentialsDTO credDTO) {
		this.credDTO = credDTO;
	}
	
	

}