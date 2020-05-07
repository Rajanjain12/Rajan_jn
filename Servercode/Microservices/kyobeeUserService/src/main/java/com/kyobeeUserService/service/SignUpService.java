package com.kyobeeUserService.service;

import java.util.List;

import com.kyobeeUserService.dto.OrganizationDTO;
import com.kyobeeUserService.dto.OrganizationTypeDTO;

public interface SignUpService {

	public OrganizationDTO addBusiness(OrganizationDTO organizationDTO);

	public List<OrganizationTypeDTO> fetchAllOrganizationType();

}
