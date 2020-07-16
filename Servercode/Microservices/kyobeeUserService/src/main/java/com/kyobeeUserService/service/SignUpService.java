package com.kyobeeUserService.service;

import java.util.List;

import com.kyobeeUserService.dto.OrganizationDTO;
import com.kyobeeUserService.dto.OrganizationTypeDTO;
import com.kyobeeUserService.dto.TimezoneDTO;

public interface SignUpService {

	public OrganizationDTO addBusiness(OrganizationDTO organizationDTO);

	public List<OrganizationTypeDTO> fetchAllOrganizationType();
	
	public List<TimezoneDTO> fetchTimezoneList();

}
