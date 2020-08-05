package com.kyobeeAccountService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeAccountService.entity.OrganizationSubscriptionDetail;

@Repository
@Transactional
public interface OrganizationSubscriptionDetailDAO extends JpaRepository<OrganizationSubscriptionDetail,Integer> {
	
	@Query(value="SELECT SubscriptionID, GROUP_CONCAT(PlanFeatureChargeID SEPARATOR ',') FROM KyobeeTechRevamp.ORGANIZATIONSUBSCRIPTIONDETAIL osd join KyobeeTechRevamp.ORGANIZATIONSUBSCRIPTION os on osd.SubscriptionID = os.OrganizationSubscriptionID WHERE os.InvoiceStatus ='Paid' and os.OrganizationID = :orgId group by SubscriptionID desc limit 6;",nativeQuery=true)
	List<Object[]> fetchInvoiceDetails(@Param("orgId") Integer orgId);
		
	@Query("FROM OrganizationSubscriptionDetail osd where osd.organizationSubscription.organizationSubscriptionID in :orgSubscId")
	List<OrganizationSubscriptionDetail> fetchPlanFeatureDetails(@Param("orgSubscId") List<Integer> orgSubscId);

}
