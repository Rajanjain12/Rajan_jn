package com.kyobeeUserService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.entity.OrganizationTemplate;

@Transactional
public interface OrganizationTemplateDAO extends CrudRepository<OrganizationTemplate,Integer>{
	
	@Query(value="select ot from OrganizationTemplate ot where ot.organization.organizationID=:orgId")
	List<OrganizationTemplate> fetchSmsTemplateForOrganization(@Param("orgId") Integer orgId);

}
