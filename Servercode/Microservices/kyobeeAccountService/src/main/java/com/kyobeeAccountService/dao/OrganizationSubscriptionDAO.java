package com.kyobeeAccountService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeAccountService.entity.OrganizationSubscription;


@Repository
@Transactional
public interface OrganizationSubscriptionDAO extends JpaRepository<OrganizationSubscription,Integer>{

	@Query(value="select * from ORGANIZATIONSUBSCRIPTION where OrganizationID=:orgId order by organizationSubscriptionID desc limit 6", nativeQuery = true)
	List<OrganizationSubscription> fetchInvoiceDetails(@Param("orgId") Integer orgId);
}
