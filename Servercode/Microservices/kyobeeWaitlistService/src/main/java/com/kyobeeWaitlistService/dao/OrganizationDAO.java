package com.kyobeeWaitlistService.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeWaitlistService.dto.SmsDetailsDTO;
import com.kyobeeWaitlistService.entity.Organization;

@Repository
@Transactional
public interface OrganizationDAO extends CrudRepository<Organization, Integer> {

	@Query(value="select waitTime from Organization where organizationID =:orgId")
	Integer getOrganizationWaitTime(@Param("orgId") Integer orgId);
	
	@Query("select new com.kyobeeWaitlistService.dto.SmsDetailsDTO(o.smsSignature,o.smsRoute,o.smsRouteNo) from Organization o where o.organizationID =:orgId")
	public SmsDetailsDTO getSmsDetails(@Param("orgId") Integer orgId);
	
	
}
