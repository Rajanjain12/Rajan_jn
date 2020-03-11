package com.kyobeeWaitlistService.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kyobeeWaitlistService.entity.OrganizationLang;

public interface OrganizationLanguageDAO extends CrudRepository<OrganizationLang, Integer>{
	
	@Modifying
	@Query(value = "delete from OrganizationLang ol where ol.organization.organizationID=:orgId and ol.langmaster.langID=:languageID")
	void deleteOrgLanguage(@Param("orgId") Integer orgId,@Param("languageID") Integer languageID);

}
