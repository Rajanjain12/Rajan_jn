package com.kyobeeWaitlistService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.kyobeeWaitlistService.entity.OrganizationTemplate;

public interface OrganizationTemplateDAO extends CrudRepository<OrganizationTemplate, Integer> {

	@Query(value = "select ot from OrganizationTemplate ot where ot.organization.organizationID=:orgId and ot.languageID=:languageID and ot.active=1")
	List<OrganizationTemplate> getSmsTemplatesForOrganization(@Param("orgId") Integer orgId,
			@Param("languageID") Integer languageID);

}
