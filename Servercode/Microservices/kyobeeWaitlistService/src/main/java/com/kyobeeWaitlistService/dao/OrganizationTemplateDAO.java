package com.kyobeeWaitlistService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.entity.OrganizationTemplate;

@Transactional
public interface OrganizationTemplateDAO extends CrudRepository<OrganizationTemplate, Integer> {

	@Query(value = "select ot from OrganizationTemplate ot where ot.organization.organizationID=:orgId and ot.languageID=:languageID and ot.active=1")
	List<OrganizationTemplate> getSmsTemplatesForOrganization(@Param("orgId") Integer orgId,
			@Param("languageID") Integer languageID);
	
	@Query(value="select ot from OrganizationTemplate ot where ot.organization.organizationID=:orgId and ot.languageID=:languageID  and ot.active=1 and ot.level=:level")
	OrganizationTemplate fetchSmsTemplateForOrgByLevel(@Param("orgId") Integer orgId,@Param("languageID") Integer languageID,@Param("level") Integer level);
	
	@Modifying
	@Query(value = "delete from OrganizationTemplate ot where ot.organization.organizationID=:orgId and ot.languageID=:languageID")
	void deleteOrgTemplate(@Param("orgId") Integer orgId,@Param("languageID") Integer languageID);
	
	@Modifying
	@Query(value="update OrganizationTemplate ot set templateText=:templateText where ot.organization.organizationID=:orgId and ot.languageID=:languageID  and ot.active=1 and ot.level=:level")
	void updateSmsTemplateForOrgByLevel(@Param("orgId") Integer orgId,@Param("languageID") Integer languageID,@Param("level") Integer level,@Param("templateText") String templateText);
	
	

}