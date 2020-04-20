package com.kyobeeUserService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.entity.OrganizationSubscription;

@Repository
@Transactional
public interface OrganizationSubscriptionDAO extends JpaRepository<OrganizationSubscription,Integer>{

}
