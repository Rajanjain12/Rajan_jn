package com.kyobeeUserService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.kyobeeUserService.entity.PlanFeatureCharge;

@Repository
@Transactional
public interface PlanFeatureChargeDAO extends JpaRepository<PlanFeatureCharge, Integer>{

	@Query("SELECT p FROM PlanFeatureCharge p WHERE  p.currency.currencyID =:currencyID order by p.planFeatureChargeID")
	List<PlanFeatureCharge> fetchPlanCharge(@Param("currencyID") Integer currencyID);
	
	List<PlanFeatureCharge> findByPlanFeatureChargeIDIn(List<Integer> lookupIDs);
}
