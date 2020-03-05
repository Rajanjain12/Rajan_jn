package com.kyobeeUserService.dao;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kyobeeUserService.entity.Plan;
@Transactional
public interface PlanDAO extends CrudRepository<Plan, Integer>{

	@Query("select p from Plan p where p.planId=:planId")
	Plan fetchPlan(@Param("planId") Integer planId);
	
}
