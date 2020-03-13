package com.kyobeeWaitlistService.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kyobeeWaitlistService.entity.OrganizationCategory;



public interface OrganizationCategoryDAO extends CrudRepository<OrganizationCategory, Integer>{
	
	@Modifying
	@Query(value = "delete from OrganizationCategory oc where oc.organization.organizationID=:orgId")
	void deleteOrgPreference(@Param("orgId") Integer orgId);

}
