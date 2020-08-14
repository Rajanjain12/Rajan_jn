package com.kyobeeAccountService.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeAccountService.entity.PlanFeatureCharge;

@Repository
@Transactional
public interface PlanFeatureChargeDAO extends JpaRepository<PlanFeatureCharge, Integer>{

	List<PlanFeatureCharge> findByPlanFeatureChargeIDIn(List<Integer> lookupIDs);
}
