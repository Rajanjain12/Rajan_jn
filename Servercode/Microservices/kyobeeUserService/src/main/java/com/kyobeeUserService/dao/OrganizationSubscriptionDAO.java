package com.kyobeeUserService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.entity.OrganizationSubscription;

@Repository
@Transactional
public interface OrganizationSubscriptionDAO extends JpaRepository<OrganizationSubscription,Integer>{

	@Modifying
	@Query("update OrganizationSubscription set invoiceStatus =:invoiceStatus, invoiceFile=:invoiceFile where organizationSubscriptionID =:orgSubscId")
	void updateInvoiceDetails(@Param("invoiceStatus") String invoiceStatus , @Param("invoiceFile") String invoiceFile ,@Param("orgSubscId") Integer orgSubscId);
}
