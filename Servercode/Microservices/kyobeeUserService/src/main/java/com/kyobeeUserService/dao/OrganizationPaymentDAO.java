package com.kyobeeUserService.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kyobeeUserService.entity.OrganizationPayment;

@Repository
@Transactional
public interface OrganizationPaymentDAO extends JpaRepository<OrganizationPayment, Integer> {
		
	@Query(value = "select op from OrganizationPayment op where op.organization.organizationID=:orgId and op.organizationSubscription.organizationSubscriptionID=:orgSubscId")
	OrganizationPayment fetchOrgPaymentDetails(@Param("orgId") Integer orgId,@Param("orgSubscId") Integer orgSubscId);


}
