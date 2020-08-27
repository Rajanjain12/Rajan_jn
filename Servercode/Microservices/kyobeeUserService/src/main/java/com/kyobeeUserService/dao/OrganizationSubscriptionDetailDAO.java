package com.kyobeeUserService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.entity.OrganizationSubscriptionDetail;

@Repository
@Transactional
public interface OrganizationSubscriptionDetailDAO extends JpaRepository<OrganizationSubscriptionDetail,Integer> {

	@Modifying
	@Query("update OrganizationSubscriptionDetail osd set currentActiveSubscription ='N',active= 0 where startDate!=CURDATE() and osd.organizationSubscription.organizationSubscriptionID =:orgSubscId")
	void updateSubscriptionDetails(@Param("orgSubscId") Integer orgSubscId);
}
