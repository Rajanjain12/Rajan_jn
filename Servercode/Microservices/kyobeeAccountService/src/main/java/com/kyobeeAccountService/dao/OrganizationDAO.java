package com.kyobeeAccountService.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeAccountService.entity.Organization;
import com.kyobeeAccountService.util.AccountServiceConstants;

@Repository
@Transactional
public interface OrganizationDAO extends JpaRepository<Organization, Integer>{

	@Modifying
	@Query("update Organization set active="+ AccountServiceConstants.INACTIVE_USER+" where organizationID =:orgId")
	void deleteOrganization(@Param("orgId") Integer orgId);
}
