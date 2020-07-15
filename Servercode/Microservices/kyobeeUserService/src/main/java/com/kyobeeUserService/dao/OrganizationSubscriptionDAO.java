package com.kyobeeUserService.dao;

import java.math.BigDecimal;

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
	
	@Modifying
	@Query("update OrganizationSubscription set discountAmt =:discountAmt, TotalBillAmount=:TotalBillAmount where organizationSubscriptionID =:orgSubscId")
	void updateBillingPrice(@Param("orgSubscId") Integer orgSubscId, @Param("discountAmt") BigDecimal discountAmt,@Param("TotalBillAmount") BigDecimal TotalBillAmount);
}
