package com.kyobeeUserService.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kyobeeUserService.entity.OrganizationCardDetail;

@Repository
@Transactional
public interface OrganizationCardDetailDAO extends JpaRepository<OrganizationCardDetail, Integer> {

	@Query("select organizationCardDetailID from OrganizationCardDetail oc where oc.organization.organizationID=:orgId and oc.customer.customerID=:customerId and cardNo=:cardNo")
	public Integer fetchOrgCardDetails(@Param("orgId") Integer orgId, @Param("customerId") Integer customerId,
			@Param("cardNo") Integer cardNo);
}
