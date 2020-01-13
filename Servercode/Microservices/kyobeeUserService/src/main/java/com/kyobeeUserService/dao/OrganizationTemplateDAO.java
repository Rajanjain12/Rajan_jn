package com.kyobeeUserService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kyobeeUserService.entity.OrganizationTemplate;

public interface OrganizationTemplateDAO extends CrudRepository<OrganizationTemplate,Integer>{
	
	@Query(value="select ot from OrganizationTemplate ot where ot.organization.organizationID=:orgId")
	List<OrganizationTemplate> fetchSmsTemplateForOrganization(@Param("orgId") Integer orgId);

}
