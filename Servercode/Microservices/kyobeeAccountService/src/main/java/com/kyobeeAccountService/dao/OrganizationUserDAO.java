package com.kyobeeAccountService.dao;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeAccountService.entity.OrganizationUser;
import com.kyobeeAccountService.util.AccountServiceConstants;

@Repository
@Transactional
public interface OrganizationUserDAO extends CrudRepository<OrganizationUser,Integer>{

	@Modifying
	@Query("update OrganizationUser ou set active="+ AccountServiceConstants.INACTIVE_USER+" where ou.user.userID =:userId and ou.organization.organizationID=:orgId")
	void deleteOrganizationUser(@Param("userId") Integer userId,@Param("orgId") Integer orgId);
	

}
