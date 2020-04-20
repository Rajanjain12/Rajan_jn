package com.kyobeeUserService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kyobeeUserService.entity.OrganizationSubscriptionDetail;

@Repository
@Transactional
public interface OrganizationSubscriptionDetailDAO extends JpaRepository<OrganizationSubscriptionDetail,Integer> {

}
