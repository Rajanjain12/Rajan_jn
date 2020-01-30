package com.kyobeeWaitlistService.dao;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.kyobeeWaitlistService.entity.Organization;

@Repository
public interface OrganizationDAO extends CrudRepository<Organization, Integer> {

	@Procedure(name = "RESETGUESTBYORGID")
	public void resetOrganizationByOrgId(@Param("ORGID") Long orgid);

}
