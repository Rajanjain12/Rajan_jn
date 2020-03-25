package com.kyobeeWaitlistService.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.entity.Organization;

@Repository
@Transactional
public interface OrganizationDAO extends CrudRepository<Organization, Integer> {

	@Query(value="select waitTime from Organization where organizationID =:orgId")
	Integer getOrganizationWaitTime(@Param("orgId") Integer orgId);
	
	@Query("select o.smsSignature from Organization o where o.organizationID =:orgId")
	public String getSmsDetails(@Param("orgId") Integer orgId);
	
	public Organization findByOrganizationID(Integer orgId);
	
	@Modifying
	@Query("update Organization set notifyUserCount =:notifyFirst,defaultLangId=:defaultLanguage,pplBifurcation=:pplBifurcation where organizationID =:orgId")
	void updateOrgSetting(@Param("notifyFirst") Integer notifyFirst,@Param("orgId") Integer orgId,@Param("defaultLanguage") Integer defaultLanguage,@Param("pplBifurcation") String pplBifurcation);
	
	
	
	
}
