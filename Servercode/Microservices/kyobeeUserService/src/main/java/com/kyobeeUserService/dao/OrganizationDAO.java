package com.kyobeeUserService.dao;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.entity.Organization;

@Repository
@Transactional
public interface OrganizationDAO extends CrudRepository<Organization, Integer>{


	@Query("select o from Organization o join o.organizationusers ou where ou.user.userID=?1 ")
	public Organization fetchOrganizationByUserId(Integer userId);
}
